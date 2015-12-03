import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/*
 * CfgBase.java
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


public abstract class CfgBase {
    private static final String TAG = "CfgBase";

    protected String cfg;
    protected String mFileName;

    public CfgBase(String name) {
        try {
            mFileName = name;

            File fp = new File(".", name);

            if (!fp.exists()) {
                FileOutputStream os = new FileOutputStream(fp);
                os.write(defaultValue().getBytes());
                os.close();
            }

            byte[] data = new byte[4096];
            FileInputStream is = new FileInputStream(fp);
            is.read(data);
            is.close();

            cfg = new String(data, "UTF-8");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public abstract String defaultValue();
}
