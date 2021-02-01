
package com.skteam.ititest.restModel.signup;

import java.util.List;
import com.google.gson.annotations.Expose;


public class ResponseSignUp {

    @Expose
    private String code;
    @Expose
    private List<Re> res;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Re> getRes() {
        return res;
    }

    public void setRes(List<Re> res) {
        this.res = res;
    }

}
