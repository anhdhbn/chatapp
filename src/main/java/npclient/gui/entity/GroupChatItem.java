package npclient.gui.entity;

public class GroupChatItem extends ChatItem {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GroupChatItem) {
            GroupChatItem oG = (GroupChatItem) obj;
            return oG.name.equals(name);
        }

        return false;
    }
}
