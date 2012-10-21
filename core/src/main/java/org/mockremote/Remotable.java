package org.mockremote;

/**
 * Implementations can proxy method invocations to an attached remote object.
 */
public interface Remotable<T> {

    void attachRemote(T remoteObject);

    void switchRemoteModeOn();

    void switchRemoteModeOff();

}
