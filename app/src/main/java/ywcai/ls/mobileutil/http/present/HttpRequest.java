package ywcai.ls.mobileutil.http.present;


import ywcai.ls.mobileutil.http.model.HttpPing;

public class HttpRequest implements HttpRequestInf {

    @Override
    public boolean updatePingResult() {
        HttpPing req=new HttpPing();
        return req.execute();
    }
}
