package org.http4k.connect.amazon.s3.action

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import org.http4k.connect.Http4kConnectAction
import org.http4k.connect.RemoteFailure
import org.http4k.connect.amazon.model.BucketKey
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Uri
import java.io.InputStream

@Http4kConnectAction
data class GetKey(val key: BucketKey) : S3BucketAction<InputStream?> {
    override fun toRequest() = Request(GET, uri())

    override fun toResult(response: Response) = with(response) {
        when {
            status.successful -> Success(body.stream)
            status == Status.NOT_FOUND -> Success(null)
            else -> Failure(RemoteFailure(GET, uri(), status))
        }
    }

    private fun uri() = Uri.of("/${key}")
}
