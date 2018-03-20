package com.zou.core;

import com.zou.core.reflection.ReflectionHelper;
import com.zou.netty.executor.MessageReceiveExecutor;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AbilityDetailProvider implements AbilityDetail {


    private final static String STYLE = "<style type=\"text/css\">\n" +
            "table.gridtable {\n" +
            "    font-family: verdana,arial,sans-serif;\n" +
            "    font-size:11px;\n" +
            "    color:#333333;\n" +
            "    border-width: 1px;\n" +
            "    border-color: #666666;\n" +
            "    border-collapse: collapse;\n" +
            "}\n" +
            "table.gridtable th {\n" +
            "    border-width: 1px;\n" +
            "    padding: 8px;\n" +
            "    border-style: solid;\n" +
            "    border-color: #666666;\n" +
            "    background-color: #dedede;\n" +
            "}\n" +
            "table.gridtable td {\n" +
            "    border-width: 1px;\n" +
            "    padding: 8px;\n" +
            "    border-style: solid;\n" +
            "    border-color: #666666;\n" +
            "    background-color: #ffffff;\n" +
            "}\n" +
            "</style>";

    private final static String HEADER = "<table class=\"gridtable\">\n" +
            "<tr>\n" +
            "    <th>NettyRPC Ability Detail</th>\n" +
            "</tr>";


    private final static String TAIL = "</table>";

    private final static String CELL_BEGIN = "<tr><td>";

    private final static String CELL_END = "</td></tr>";


    @Override
    public StringBuilder listAbilityDetail(boolean html) {

        Map<String, Object> map = MessageReceiveExecutor.getInstance().getHandlerMap();

        ReflectionHelper helper = new ReflectionHelper();

        if (html) {
            helper.getProvider().append(STYLE).append(HEADER);
        }

        Set<String> set = (Set<String>) map.keySet();

        Iterator<String> iterator = set.iterator();

        String key;

        while (iterator.hasNext()) {

            key = iterator.next();

            try {

                if (html) {

                    helper.getProvider().append(CELL_BEGIN);
                    helper.listRpcProviderDetail(Class.forName(key), html);
                    helper.getProvider().append(CELL_END);
                } else {

                    helper.listRpcProviderDetail(Class.forName(key), html);
                }

            } catch (ClassNotFoundException e) {

                e.printStackTrace();

            }

        }


        if (html) {
            helper.getProvider().append(TAIL);
        }

        return helper.getProvider();

    }
}






































































