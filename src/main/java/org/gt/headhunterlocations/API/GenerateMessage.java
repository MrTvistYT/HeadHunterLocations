package org.gt.headhunterlocations.API;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class GenerateMessage {
    public static TextComponent generateAcceptDecline(){
        TextComponent textComponent1 = new TextComponent("§7[§2Принять§7]");
        textComponent1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/headhunter accept"));

        TextComponent textComponent2 = new TextComponent("§7[§4Отклонить§7]");
        textComponent2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/headhunter cancel"));

        textComponent1.addExtra(" ");
        textComponent1.addExtra(textComponent2);

        return textComponent1;
    }

    public static TextComponent generateYesNo(){
        TextComponent textComponent1 = new TextComponent("§7[§2Да§7]");
        textComponent1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/headhunter createTask"));

        TextComponent textComponent2 = new TextComponent("§7[§4Нет§7]");
        textComponent2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/headhunter cancelCreating"));

        textComponent1.addExtra(" ");
        textComponent1.addExtra(textComponent2);

        return textComponent1;
    }
}
