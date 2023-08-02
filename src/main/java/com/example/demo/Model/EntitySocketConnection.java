package com.example.demo.Model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Getter
@Setter
@NoArgsConstructor
@FieldNameConstants
public class EntitySocketConnection {

    @Id
    private String id;

    private String inboxId;

    private String label;

    private String threadStatus;

    private String nameSpace;

    private String threadId;

    private String threadQuery;

    private long mostSigBits;

    private long leastSigBits;

    private String sessionId;

    private Date connectedTime;

    private String currentMsgService;

    private long userId;

    public EntitySocketConnection(long mostSigBits, long leastSigBits, String sessionId, Date connectedTime, long userId) {
        this.mostSigBits = mostSigBits;
        this.leastSigBits = leastSigBits;
        this.sessionId = sessionId;
        this.connectedTime = connectedTime;
        this.userId = userId;
    }
}
