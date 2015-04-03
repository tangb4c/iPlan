package me.plan.HomePage.data.element;

/**
 * Created by tangb4c on 2015/3/30.
 */
public class AttachInfo {
    public int from, to;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AttachInfo{");
        sb.append("\"from\":").append(from);
        sb.append(",\"to\"=").append(to);
        sb.append('}');
        return sb.toString();
    }
}
