package silentorb.metahub.database

import silentorb.imp.core.*
import silentorb.imp.execution.CompleteFunction
import silentorb.imp.execution.newLibrary
import silentorb.imp.execution.typePairsToTypeNames
import silentorb.imp.library.standard.standardLibrary

const val queryPath = "silentorb.metahub.querying"

val entriesTransformType = newTypePair(PathKey(queryPath, "EntriesTransform"))

fun impQueryFunctions(): List<CompleteFunction> = listOf(
    CompleteFunction(
        path = PathKey(queryPath, "all"),
        signature = CompleteSignature(
            output = entriesTransformType
        ),
        implementation = {
          val result: QueryFunction = { entries ->
            queryAll(entries)
          }
          result
        }
    ),
)

fun queryTypes() =
    typePairsToTypeNames(
        listOf(
            entriesTransformType
        )
    )

fun newQueryLibrary() =
    newLibrary(
        functions = impQueryFunctions(),
        typeNames = queryTypes()
    )

fun newImpLibrary(): Namespace =
    mergeNamespaces(
        defaultImpNamespace(),
        standardLibrary(),
        newQueryLibrary(),
    )
