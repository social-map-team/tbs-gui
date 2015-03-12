package com.socialmap.server.share;

import java.util.Set;

/**
 * Created by yy on 3/9/15.
 */
public class SosRequest {
    private SosCaller caller;
    private Set<SosTarget> targets;
    private String reason;

    public SosCaller getCaller() {
        return caller;
    }

    public void setCaller(SosCaller caller) {
        this.caller = caller;
    }

    public Set<SosTarget> getTargets() {
        return targets;
    }

    public void setTargets(Set<SosTarget> targets) {
        this.targets = targets;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
