package mafia.commandThings;

import java.io.Serializable;
/** an enum for the type of the commands
 */
public enum CommandTypes implements Serializable {
    //server to client command types:
    determineYourUserName,
    repetitiousUserName,
    yourUserNameIsSet,
    takeYourRole,
    vote,
    votingResult,
    doYourAction,
    itIsNight,
    newMessage,
    chatRoomStarted,
    chatRoomIsClosed,
    serverToClientString,
    waitingForClientToGetReady,
    youAreDead,
    endOfTheGame,
    youAreMutedForTomorrow,
    // client to server command type :
    setMyUserName,
    iExitTheGame,
    messageToOthers,
    imReady, // dont want to chat any more
    iVote,
    iDoMyAction,
    mayorSaysLynch,
    mayorSaysDontLynch,
}
