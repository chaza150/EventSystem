package Events.Consumers;

import Events.Event;
import Events.TopicEnum;

public class ParserConsumer extends EventConsumer{

    @Override
    protected void processEvent(Event e) {
        parseEventText(e.getText(), e.getEventTimeMillis());
    }

    private void parseEventText(String text){
        parseEventText(text, System.currentTimeMillis());
    }

    private void parseEventText(String text, long eventTime){
        String[] lines = text.split("\n", 2);

        String line = lines[0];
        while(lines.length > 0) {
            line = lines[0];
            if (!line.equals("")) {
                break;
            } else {
                lines = text.split("\n", 2);
            }
        }
        if(lines.length == 0){
            return;
        }

        if(line.startsWith("SUBMIT ")){
            String[] lineElements = line.split(" ");
            if(lineElements.length > 2){
                TopicEnum topicEnum = TopicEnum.valueOf(lineElements[1]);
                String searchText;
                if(lines.length > 1){
                    searchText = line + "\n" + lines[1];
                } else {
                    searchText = line;
                }
                String eventText = getStringBetweenBraces(searchText);
                Event newEvent = new Event(topicEnum, eventText);
                //TODO: Add event to queue
                //TODO: Get remainder of text and do it again
            }
        } else if(line.startsWith("AFTER ")){
            String[] lineElements = line.split(" ");
            if(lineElements.length > 2){

                int afterTime = Integer.parseInt(lineElements[1]);
                String searchText;
                if(lines.length > 1){
                    searchText = line + "\n" + lines[1];
                } else {
                    searchText = line;
                }
                String messageText = getStringBetweenBraces(searchText);
                parseEventText(messageText, eventTime + afterTime);
            }
        } else if(line.startsWith("AT ")){
            String[] lineElements = line.split(" ");
            if(lineElements.length > 2){

                long atTime = Long.parseLong(lineElements[1]);
                String searchText;
                if(lines.length > 1){
                    searchText = line + "\n" + lines[1];
                } else {
                    searchText = line;
                }
                String messageText = getStringBetweenBraces(searchText);
                parseEventText(messageText, atTime);
            }
        } else if(line.startsWith("EVERY ")){
            String[] lineElements = line.split(" ");
            if(lineElements.length > 2){

                int everyTime = Integer.parseInt(lineElements[1]);
                String searchText;
                if(lines.length > 1){
                    searchText = line + "\n" + lines[1];
                } else {
                    searchText = line;
                }
                String messageText = getStringBetweenBraces(searchText);
                //TODO: Set up and register a scheduler to send parsable events
                //parseEventText(messageText, atTime);
            }
        }
    }

    private String getStringBetweenBraces(String text){
        String result = "";
        int braceDepth = 0;
        for(int i = 0, n = text.length() ; i < n ; i++) {
            char c = text.charAt(i);
            if(c == '{'){
                braceDepth++;
            } else if (c == '}'){
                braceDepth--;
            }
            if(braceDepth > 0 && !(braceDepth == 1 && c == '{')){
                result += c;
            }
            if(braceDepth == 0){
                return result;
            }
        }
        return result;
    }

}
