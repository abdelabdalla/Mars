/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.Abdel.myapplication.backend;

/*
*Copyright 2016 Abdel-Rahim Abdalla
*/

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

@Api(
  name = "myApi",
  version = "v1",
  namespace = @ApiNamespace(
    ownerDomain = "backend.myapplication.Abdel.example.com",
    ownerName = "backend.myapplication.Abdel.example.com",
    packagePath=""
  )
)
public class MyEndpoint {

    @ApiMethod(name = "sayHi")
    public MyBean sayHi() {
        MyBean response = new MyBean();

            String json = "{\n" +
                    "  \"date\":\"31/3/2016\",\n" +
                    "  \"entries\":[\n" +
                    "    [\"RAF Recruits\",\"Blues\",\"Sgt T Hard\",\"MRAF Cox\",\"Drill\"],\n" +
                    "    [\"Army Recruits\",\"MTP\",\"CSM D Sack\",\"5Lt Dorris\",\"Drill\"],\n" +
                    "    [\"RAF Advanced\",\"Blues\",\"LCpl No '1' Cares\",\"F5Lt Cox\",\"U5Ltilearn Stuff. In e4\"],\n" +
                    "    [\"Army Advanced\",\"MTP\",\"FSgt L Brioche\",\"5Lt Dorris\",\"Wait for LSW's\"],\n" +
                    "    [\"REME\",\"Blues/MTP\",\"SSgt N V Keen\",\"5Lt Dorris\",\"Presentations with Cpl A Awesome\"],\n" +
                    "    [\"Signals\",\"Blues/MTP\",\"Cpl V Keen\",\"MRAF Cox\",\"Do something useless as usual\"]\n" +
                    "    ]\n" +
                    "}";

            response.setData(json);

        return response;
    }

}
