package data

import kotlinproject.composeapp.generated.resources.*
import model.Node

object SampleTree {

    val root = Node(
        id = "root",
        title = "My Dashboard", //top bar title
        icon = Res.drawable.ic_home,
        children = listOf(
            Node(
                id = "home",
                title = "Home",
                icon = Res.drawable.ic_home
            ),
            Node(
                id = "vehicle",
                title = "Vehicle",
                icon = Res.drawable.ic_directions_car
            ),
            Node(
                id = "financial",
                title = "Financial",
                icon = Res.drawable.ic_attach_money
            ),
            Node(
                id = "business",
                title = "Business",
                icon = Res.drawable.ic_corporate_fare
            )
        )
    )
}