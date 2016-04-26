/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package net.ddns.sabr.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

/** An endpoint class we are exposing */
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

    private String json  = "{\n" +
            "  \"date\":\"14/4/2016\",\n" +
            "  \"entries\":[\n" +
            "    [\"RAF Recruits\",\"Blues\",\"Sgt T Hard\",\"MRAF Cox\",\"Drill\"],\n" +
            "    [\"Army Recruits\",\"MTP\",\"CSM D Sack\",\"5Lt Dorris\",\"Drill\"],\n" +
            "    [\"RAF Advanced\",\"Blues\",\"LCpl No '1' Cares\",\"MRAF Cox\",\"Ultilearn Stuff. In e4\"],\n" +
            "    [\"Army Advanced\",\"MTP\",\"FSgt L Brioche\",\"5Lt Dorris\",\"Wait for LSW's\"],\n" +
            "    [\"REME\",\"Blues/MTP\",\"SSgt N V Keen\",\"5Lt Dorris\",\"Presentations with Cpl A Awesome\"],\n" +
            "    [\"Signals\",\"Blues/MTP\",\"Cpl V Keen\",\"MRAF Cox\",\"Do something useless as usual\"]\n" +
            "    ]\n" +
            "}";

    private String file = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<body>\n" +
            "\n" +
            "<h1>My First Heading</h1>\n" +
            "\n" +
            "<p>My first paragraph.</p>\n" +
            "\n" +
            "</body>\n" +
            "</html>";

    @ApiMethod(name = "download")
    public Bean download() {
        Bean response = new Bean();
        response.setData(json);
        response.setFile(file);
        return response;
    }

    @ApiMethod(name = "upload")
    public Bean upload(@com.google.api.server.spi.config.Named("string") String string){
        Bean response = new Bean();
        json = string;
        response.setData("done");
        return response;
    }

    @ApiMethod(name = "resetFile")
    public Bean resetFile(){
        Bean response = new Bean();
        file = "";
        response.setData("done");
        return response;
    }

    @ApiMethod(name = "addToFile")
    public Bean addToFile(@com.google.api.server.spi.config.Named("f") String f){
        Bean response = new Bean();
        file += f;
        response.setData("done");
        return response;
    }

    /* = "{\n" +
            "  \"date\":\"31/3/2016\",\n" +
            "  \"entries\":[\n" +
            "    [\"RAF Recruits\",\"Blues\",\"Sgt T Hard\",\"MRAF Cox\",\"Drill\"],\n" +
            "    [\"Army Recruits\",\"MTP\",\"CSM D Sack\",\"5Lt Dorris\",\"Drill\"],\n" +
            "    [\"RAF Advanced\",\"Blues\",\"LCpl No '1' Cares\",\"MRAF Cox\",\"U5Ltilearn Stuff. In e4\"],\n" +
            "    [\"Army Advanced\",\"MTP\",\"FSgt L Brioche\",\"5Lt Dorris\",\"Wait for LSW's\"],\n" +
            "    [\"REME\",\"Blues/MTP\",\"SSgt N V Keen\",\"5Lt Dorris\",\"Presentations with Cpl A Awesome\"],\n" +
            "    [\"Signals\",\"Blues/MTP\",\"Cpl V Keen\",\"MRAF Cox\",\"Do something useless as usual\"]\n" +
            "    ]\n" +
            "}"*/

}