/*
 * Copyright (c) 2016 truRating Limited. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * truRating Limited. ("Confidential Information").  You shall
 * not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with truRating Limited.
 *
 * TRURATING LIMITED MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT
 * THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TRURATING LIMITED
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

TRUMODULE
=========

This program should be passed the location of a resources Java properties file via the command line. You can change the location 
of this by editing the start_TruModule.bat file. The location of the resources file is passed like so:

-Dresources=C:\truModule\ 

You should also pass a log4j configuration file location, otherwise this example program will be assume a default setting
and log into the root directory of the jar file.

-Dlog4j.configuration=file:\C:\truModule\properties\truModuleLog4J.properties

THE PAYMENT APPLICATION
=======================
 
The payment application supplied is only a mock up, and is in no way intended to be a fully functioning example of such an 
application. However, it should help developers to see easily where truModule fits into their organisation's stack.
 
The interface of interest to developers, will be the ITruModule interface.