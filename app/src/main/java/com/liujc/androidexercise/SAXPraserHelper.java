package com.liujc.androidexercise;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 类名称：SAXPraserHelper
 * 创建者：Create by liujc
 * 创建时间：Create on 2017/5/15 16:00
 * 描述：TODO
 * 最近修改时间：2017/5/15 16:00
 * 修改人：Modify by liujc
 */
public class SAXPraserHelper extends DefaultHandler{
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }
}
