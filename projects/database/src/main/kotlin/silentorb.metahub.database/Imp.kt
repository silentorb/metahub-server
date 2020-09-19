package silentorb.metahub.database

import silentorb.imp.core.*
import silentorb.imp.execution.CompleteFunction
import silentorb.imp.execution.newLibrary
import silentorb.imp.execution.typePairsToTypeNames
import silentorb.imp.library.standard.standardLibrary

val allKeys: DatabaseToKeys = { database ->
  getKeys(database.triples)
}

fun expand(entities: DatabaseToKeys): DatabaseToObjects = { database ->
  val keys = entities(database)
  expandKeys(database, keys)
}

fun filter(keys: DatabaseToKeys, predicate: DatabaseKeyPredicate): DatabaseToKeys = { database ->
  keys(database)
      .filter { key -> predicate(database, key) }
      .toSet()
}

fun hasRelationship(relationship: String): DatabaseKeyPredicate = { database, key ->
  database.triples.any { entry -> entry.source == key && entry.relationship == relationship }
}

fun impQueryFunctions(): List<CompleteFunction> = listOf(

    CompleteFunction(
        path = PathKey(queryPath, "all"),
        signature = CompleteSignature(
            output = databaseToKeysType
        ),
        implementation = { allKeys }
    ),

    CompleteFunction(
        path = PathKey(queryPath, "expand"),
        signature = CompleteSignature(
            parameters = listOf(
                CompleteParameter("keys", databaseToKeysType)
            ),
            output = databaseToObjectsType
        ),
        implementation = { arguments ->
          expand(arguments["keys"]!! as DatabaseToKeys)
        }
    ),

    CompleteFunction(
        path = PathKey(queryPath, "filter"),
        signature = CompleteSignature(
            parameters = listOf(
                CompleteParameter("keys", databaseToKeysType),
                CompleteParameter("predicate", databaseKeyPredicateType)
            ),
            output = databaseToKeysType
        ),
        implementation = { arguments ->
          val keys = arguments["keys"]!! as DatabaseToKeys
          val predicate = arguments["predicate"]!! as DatabaseKeyPredicate
          filter(keys, predicate)
        }
    ),

    CompleteFunction(
        path = PathKey(queryPath, "hasRelationship"),
        signature = CompleteSignature(
            parameters = listOf(
                CompleteParameter("relationship", stringType)
            ),
            output = databaseKeyPredicateType
        ),
        implementation = { arguments ->
          val relationship = arguments["relationship"]!! as String
          hasRelationship(relationship)
        }
    ),
)

fun queryTypes() =
    typePairsToTypeNames(
        listOf(
            databaseToKeysType
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
