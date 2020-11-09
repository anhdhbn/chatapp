package npclient.gui.entity;

public class UserChatItem extends ChatItem {
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserChatItem) {
            UserChatItem oG = (UserChatItem) obj;
            return oG.name.equals(name);
        }

        return false;
    }
}
