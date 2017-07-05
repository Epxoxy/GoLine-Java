package core.interfaces;

import common.TAction;
import core.data.IntPoint;

public interface IGameCoreResolver extends IDisposable{
    boolean isUseful();
    boolean isStarted();
    boolean isHostActive();
    String getHostToken();
    String getJoinToken();
    String getWinnerToken();
    String getActiveToken();
    String getFirstToken();
    void ready();
    void start();
    void reset();
    void setFirst(String token);
    void undo();
    void setJudgedListener(JudgedLiteListener listener);
    void tips(TAction<IntPoint> onResult);
}
