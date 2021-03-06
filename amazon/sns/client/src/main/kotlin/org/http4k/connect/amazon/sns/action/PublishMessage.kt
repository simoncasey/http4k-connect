package org.http4k.connect.amazon.sns.action

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import org.http4k.connect.Http4kConnectAction
import org.http4k.connect.RemoteFailure
import org.http4k.connect.amazon.model.ARN
import org.http4k.connect.amazon.model.PhoneNumber
import org.http4k.connect.amazon.model.SNSMessageId
import org.http4k.connect.amazon.model.asList
import org.http4k.connect.amazon.model.text
import org.http4k.connect.amazon.model.textOptional
import org.http4k.connect.amazon.model.xmlDoc
import org.http4k.core.Method.POST
import org.http4k.core.Response

@Http4kConnectAction
data class PublishMessage(
    val message: String,
    val subject: String? = null,
    val phoneNumber: PhoneNumber? = null,
    val topicArn: ARN? = null,
    val targetArn: ARN? = null,
    val messageDeduplicationId: String? = null,
    val messageGroupId: String? = null,
    val messageStructure: String? = null,
    val attributes: List<MessageAttribute>? = null
) : SNSAction<PublishedMessage>(
    "Publish",
    *(
        asList(attributes ?: emptyList()) +
            listOfNotNull(
                "Message" to message,
                messageStructure?.let { "MessageStructure" to it },
                messageDeduplicationId?.let { "MessageDeduplicationId" to it },
                messageGroupId?.let { "MessageGroupId" to it },
                topicArn?.let { "TopicArn" to it.value },
                targetArn?.let { "TargetArn" to it.value },
                phoneNumber?.let { "PhoneNumber" to it.value },
            )
        ).toTypedArray()
) {
    override fun toResult(response: Response) = with(response) {
        when {
            status.successful -> Success(PublishedMessage.from(response))
            else -> Failure(RemoteFailure(POST, uri(), status))
        }
    }
}

data class PublishedMessage(
    val MessageId: SNSMessageId,
    val SequenceNumber: String? = null
) {
    companion object {
        fun from(response: Response) =
            with(response.xmlDoc()) {
                PublishedMessage(
                    SNSMessageId.of(text("MessageId")),
                    textOptional("SequenceNumber")
                )
            }
    }
}

