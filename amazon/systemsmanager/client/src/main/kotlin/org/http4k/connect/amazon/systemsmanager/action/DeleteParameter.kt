package org.http4k.connect.amazon.systemsmanager.action

import org.http4k.connect.Http4kConnectAction
import org.http4k.connect.amazon.model.SSMParameterName
import se.ansman.kotshi.JsonSerializable

@Http4kConnectAction
@JsonSerializable
data class DeleteParameter(val Name: SSMParameterName) : SystemsManagerAction<Unit>(Unit::class)
