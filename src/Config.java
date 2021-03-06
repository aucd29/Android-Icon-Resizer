/*
 * Config.java
 * Copyright 2013 Burke Choi All rights reserved.
 *             http://www.sarangnamu.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class Config extends CfgBase {
    private static final String TAG = "Config";

    public String iconName;
    public HashMap<String, Integer> iconType;

    public Config() {
        super("cfg.json");

        if (cfg == null) {
            return ;
        }

        iconType = new HashMap<String, Integer>();

        try {
            JSONObject json = new JSONObject(cfg);
            iconName = json.getString("name");

            JSONArray arr = json.getJSONArray("type");
            int len = arr.length();
            for (int i=0; i<len; ++i) {
                JSONObject type = arr.getJSONObject(i);
                iconType.put(type.getString("name"), type.getInt("size"));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String defaultValue() {
        return "{" +
                "\"name\":\"ic_launcher.png\",\r\n" +
                "\"type\":[\r\n" +
                "{\"name\":\"ldpi\", \"size\":36},\r\n" +
                "{\"name\":\"mdpi\", \"size\":48},\r\n" +
                "{\"name\":\"hdpi\", \"size\":72},\r\n" +
                "{\"name\":\"xhdpi\", \"size\":96},\r\n" +
                "{\"name\":\"xxhdpi\", \"size\":144},\r\n" +
                "{\"name\":\"xxxhdpi\", \"size\":192}\r\n" +
                "]\r\n" +
                "}";
    }
}
