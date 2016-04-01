package com.truRating.truModule;

import com.truRating.truModule.network.xml.IXMLNetworkMessenger;
import com.truRating.truModule.network.xml.XMLNetworkMessenger;
import com.truRating.truModule.device.IDevice;
import com.truRating.truModule.payment.IPaymentRequest;
import com.truRating.truModule.payment.IPaymentResponse;
import com.truRating.truModule.prize.CheckForPrize;
import com.truRating.truModule.properties.ITruModuleProperties;
import com.truRating.truModule.rating.Rating;

import com.truRating.truModule.xml.questionResponse.QuestionResponseJAXB;
import com.truRating.truModule.xml.questionResponse.QuestionResponseJAXB.Languages.Language.DisplayElements.Question;
import com.truRating.truModule.xml.ratingResponse.RatingResponseJAXB;
import com.truRating.truModule.xml.ratingResponse.RatingResponseJAXB.Languages.Language.Receipt;

import org.apache.log4j.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;

/**
 * Created by Paul on 01/03/2016.
 */
public class TruModule implements ITruModule, Runnable {

    private final Logger log = Logger.getLogger(TruModule.class);
    private int moduleStatus = ModuleStatus.SUSPENDED;
    private IXMLNetworkMessenger xmlNetworkMessenger;
    private final CheckForPrize checkForPrize = new CheckForPrize();
    private IDevice iDevice;
    private ITruModuleProperties properties = null;

    public TruModule() {

        if (!log4JIsConfigured()) {
            configureLog4JUsingDefaults();
        }
    }

    public int start() {

        if (this.properties == null)
            log.error("No properties set");
        else {
            log.info("Using the properties setProgramProperties by the payment client application");

            new Thread(this).start();
            try {
                Thread.sleep(2000); //wait for module to come up and report status
            } catch (InterruptedException e) {
                log.error(e);
            }
        }
        return moduleStatus;
    }

    public String[] preProcessQuestion(Question question) {
        String q1Pair = "truRating:\nPlease rate the\nfood from\n0-9 or clear\n" +
                "\n     truRating:\n   Please rate the\n     food from\n    0-9 or clear";

        StringTokenizer st = new StringTokenizer(q1Pair, "\n");

        String[] q1 = new String[4];
        for (int i = 0; i < 4; i++) {
            q1[i] = st.nextToken();
        }

        String[] q2 = new String[4];
        for (int i = 0; i < 4; i++) {
            q2[i] = st.nextToken();
        }

        return q1;
    }


    public Rating doRating(IPaymentRequest paymentRequest, ITruModuleProperties properties) {
        String keyStroke;

        try {
            final QuestionResponseJAXB questionResponseJAXB = xmlNetworkMessenger.getQuestionFromService(properties);

            if (questionResponseJAXB.getErrortext() != null) {
                if (!questionResponseJAXB.getErrortext().equals("")) {
                    log.error("The QuestionRequest produced an error : " + questionResponseJAXB.getErrortext());
                    return null;
                }
            }

            final Question question = questionResponseJAXB.getLanguages().getLanguage().getDisplayElements().getQuestion();
            final long startTime = System.currentTimeMillis();
            keyStroke = iDevice.displayTruratingQuestionGetKeystroke(preProcessQuestion(question), question.getValue(), 1000);
            final long endTime = System.currentTimeMillis();
            final Long totalTimeTaken = endTime - startTime;

            //if user rated then check if there is a prize
            boolean userRated = false;
            if (new Integer(keyStroke) > 0) userRated = true;

            String prizeCode = "";
            if (userRated) {
                prizeCode = checkForPrize.doCheck(getDevice(), questionResponseJAXB, prizeCode);
            }

            final Rating rating = new Rating();
            rating.setValue(new Short(keyStroke));
            rating.setRatingTime(totalTimeTaken.toString());
            if (prizeCode == null) prizeCode = ""; //for unlikely event of prizeCode check method failing
            rating.setPrizeCode(prizeCode);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            rating.setRatingDateTime(sdf.format(new Date()));
            rating.setQuestionID(new Long(question.getQid()).intValue());

            return rating;

        } catch (Exception e) {
            log.error("tru Module error", e);
        }
        return null;
    }

    public Rating doRating(ITruModuleProperties properties) {
        return doRating(null, properties);
    }

    public void cancelRating() {
        getDevice().cancelInput();
    }

    public void setDeviceRef(IDevice deviceRef) {
        this.iDevice = deviceRef;
    }

    public boolean recordRatingResponse(IPaymentResponse paymentResponse, Rating rating, ITruModuleProperties properties) {

        try {
            //send the rating
            final RatingResponseJAXB ratingResponseJAXB = xmlNetworkMessenger.deliveryRatingToService(rating, paymentResponse, properties);
            final Receipt ratingResponseReceipt =
                    ratingResponseJAXB.getLanguages().getLanguage().getReceipt();

            boolean userRated = false;
            if (rating != null) {
                if (new Integer(rating.getValue()) > 0) userRated = true;
            }
        } catch (Exception e) {
            log.error(e);
            return false;
        }

        log.info("Everything is finished in truModule!");
        return true;
    }

    private IDevice getDevice() {
        return iDevice;
    }

    public void setProgramProperties(ITruModuleProperties properties) {
        log.info("TruModule has been initialised"); // with " + properties.size() + " arguments");
        this.properties = properties;
    }

    public void run() {
        xmlNetworkMessenger = new XMLNetworkMessenger();
        moduleStatus = ModuleStatus.RUNNING;
        log.info("Module up");
    }

    public IXMLNetworkMessenger getXmlNetworkMessenger() {
        return xmlNetworkMessenger;
    }

    private void configureLog4JUsingDefaults() {
        FileAppender fileAppender = new FileAppender(); //create appender
        String PATTERN = "%d [%p|%c|%C{1}] %m%n";
        File f = new File("defaultTruModule.log");
        fileAppender.setFile("defaultTruModule.log");
        fileAppender.setAppend(true);
        fileAppender.setLayout(new PatternLayout(PATTERN));
        fileAppender.setThreshold(Level.INFO);
        fileAppender.activateOptions();
        Logger.getRootLogger().addAppender(fileAppender);
        String msg = ("\nThis program is using a default log file for logging at: " + f.getAbsolutePath() + "." +
                "\nTo avoid seeing this message, please correctly configure this option from the command line.\n");
        System.out.println(msg);
        log.warn(msg);
    }

    private static boolean log4JIsConfigured() {
        Enumeration appenders = Logger.getRootLogger().getAllAppenders();
        if (appenders.hasMoreElements()) {
            return true;
        } else {
            Enumeration loggers = LogManager.getCurrentLoggers();
            while (loggers.hasMoreElements()) {
                Logger c = (Logger) loggers.nextElement();
                if (c.getAllAppenders().hasMoreElements())
                    return true;
            }
        }
        return false;
    }

}
