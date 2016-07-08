package com.trurating.xml;

import com.trurating.service.v200.xml.Response;
import com.trurating.service.v200.xml.ResponseLanguage;

import java.util.List;
import java.util.ListIterator;

public class LanguageManager {

	public ResponseLanguage getLanguage (Response response, String languageCode) {

		List<ResponseLanguage> languages = response.getDisplay().getLanguage();
    	if (languages.size()==0)
    		return null ;
    	
    	ResponseLanguage lang = null ;
    	ListIterator<ResponseLanguage> iterator = languages.listIterator() ;
    	while (iterator.hasNext()) {
    		lang = iterator.next() ;
    		if ((lang != null) && (lang.getRfc1766().equals(languageCode)))
    				return lang;
    	}
        return lang;
	}
}
