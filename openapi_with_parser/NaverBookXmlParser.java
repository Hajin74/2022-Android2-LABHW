package mobile.exam.network.sample.openapi_with_parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


public class NaverBookXmlParser {

    // xml에서 읽어들일 태그를 구분한 enum
    private enum TagType {NONE, TITLE, AUTHOR, IMAGE_LINK};

    // parsing 대상인 tag를 상수로 선언
    private final static String ITEM_TAG = "item";
    private final static String TITLE_TAG = "title";
    private final static String AUTHOR_TAG = "author";
    private final static String IMAGE_LINK_TAG = "image";

    private XmlPullParser parser;

    public NaverBookXmlParser() {
        try {
            parser = XmlPullParserFactory.newInstance().newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<NaverBookDto> parse(String xml) {
        ArrayList<NaverBookDto> resultList = new ArrayList();
        NaverBookDto dbo = null;
        TagType tagType = TagType.NONE;     //  태그를 구분하기 위한 enum 변수 초기화

        try {
            // 파싱 대상 지정
            parser.setInput(new StringReader(xml));

            // 태그 유형 구분 변수 준비
            int eventType = parser.getEventType();

            // parsing 수행 - for 문 또는 while 문으로 구성
            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if(tag.equals(ITEM_TAG)) {
                            dbo = new NaverBookDto();
                        } else if (tag.equals(TITLE_TAG) && dbo != null) {
                            tagType = TagType.TITLE;
                        } else if (tag.equals(AUTHOR_TAG)  && dbo != null) {
                            tagType = TagType.AUTHOR;
                        } else if (tag.equals(IMAGE_LINK_TAG)  && dbo != null) {
                            tagType = TagType.IMAGE_LINK;
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals(ITEM_TAG)) {
                            resultList.add(dbo);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case TITLE:
                                dbo.setTitle(parser.getText());
                                break;
                            case AUTHOR:
                                dbo.setAuthor(parser.getText());
                                break;
                            case IMAGE_LINK:
                                dbo.setImageLink(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
