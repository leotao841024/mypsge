package manager;

import com.alibaba.wukong.im.Message;
import com.alibaba.wukong.im.MessageContent;
import  bean.MessageInfo.MssageType;
import bean.MessageInfo;

/**
 * Created by jam on 2016/3/24.
 */
public class MessageReceiver {
    public static MssageType getMessageType(int ptyp){
        int[] typvals={MessageContent.MessageContentType.TEXT,MessageContent.MessageContentType.IMAGE};
        MssageType[] typs={MssageType.TXT,MssageType.IMAGE};
        for(int i=0;i<typvals.length;i++){
            if(typvals[i]==ptyp){
                return typs[i];
            }
        }
        return MssageType.TXT;
    }

    public  static String getMessageContent(Message pmsg){
        if(pmsg==null|| pmsg.messageContent()==null){return "";}
        String content="";
        switch (pmsg.messageContent().type()) {
            case MessageContent.MessageContentType.TEXT:
                content= ((MessageContent.TextContent) pmsg.messageContent()).text();
                break;
            case MessageContent.MessageContentType.IMAGE:
                content= ((MessageContent.ImageContent) pmsg.messageContent()).url();
                break;
            default:
                break;
        }
        return content;
    }

}
