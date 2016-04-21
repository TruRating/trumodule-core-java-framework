package com.trurating.xml;

import java.util.List;
import java.util.ListIterator;

import com.trurating.xml.questionResponse.QuestionResponseJAXB;
import com.trurating.xml.ratingResponse.RatingResponseJAXB;

public class LanguageManager {

	public QuestionResponseJAXB.Languages.Language getLanguage (QuestionResponseJAXB response, String languageCode) {

		List<QuestionResponseJAXB.Languages.Language> languages = response.getLanguages().getLanguage() ;		
    	if (languages == null)
    		return null ;
    	
    	QuestionResponseJAXB.Languages.Language lang = null ; 
    	ListIterator<QuestionResponseJAXB.Languages.Language> iterator = languages.listIterator() ;
    	while (iterator.hasNext()) {
    		lang = iterator.next() ;
    		if ((lang != null) && (lang.getLanguagetype().equals(languageCode)))
    				return lang;
    		
    	}
        return lang;
	}

	public RatingResponseJAXB.Languages.Language getLanguage (RatingResponseJAXB response, String languageCode) {

		List<RatingResponseJAXB.Languages.Language> languages = response.getLanguages().getLanguage() ;		
    	if (languages == null)
    		return null ;
    	
    	RatingResponseJAXB.Languages.Language lang = null ; 
    	ListIterator<RatingResponseJAXB.Languages.Language> iterator = languages.listIterator() ;
    	while (iterator.hasNext()) {
    		lang = iterator.next() ;
    		if ((lang != null) && (lang.getLanguagetype().equals(languageCode)))
    				return lang;
    		
    	}
        return lang;
	}
}
