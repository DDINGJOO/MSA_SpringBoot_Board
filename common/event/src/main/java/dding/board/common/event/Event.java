package dding.board.common.event;


import dding.board.common.dataserializer.DataSerializer;
import lombok.Getter;

@Getter
public class Event<T extends EventPayload> {
    private Long eventId;
    private EventType type;
    private T payload;


    public static Event<EventPayload> of(Long eventId, EventType type, EventPayload paylod)
    {
        Event<EventPayload> event = new Event<>();
        event.eventId = eventId;
        event.type = type;
        event.payload = paylod;
        return  event;
    }

    public String tojson()
    {
        return DataSerializer.serialize(this);
    }

    public static Event<EventPayload> fromjson(String json)
    {

        EventRaw eventRaw = DataSerializer.deserialize(json, EventRaw.class);
        if(eventRaw == null)
        {
        return null;
        }

        Event<EventPayload> event = new Event<>();
        event.eventId = eventRaw.getEventId();
        event.type = EventType.from(eventRaw.getType());
        event.payload = DataSerializer.deserialize(eventRaw.getPayload(), event.type.getPayloadClass());
        return event;

    }

    @Getter
    private static class EventRaw{
        private Long eventId;
        private String type;
        private Object payload;
    }
}
