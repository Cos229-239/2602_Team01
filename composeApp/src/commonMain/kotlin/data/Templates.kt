package data

import model.Node
import model.Field
import model.FieldType
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource

data class Template(
    val name: String,
    val icon: DrawableResource? = null,
    val defaultNodes: List<Node>
)

//copy function
fun Node.deepCopy(): Node {
    return Node(
        id = IdGenerator.newId(),
        title = this.title,
        icon = this.icon,
        isContainer = this.isContainer,
        children = this.children.map { it.deepCopy() },
        fields = this.fields.map { it.copy() }.toMutableList(),
        documents = this.documents.toMutableList(),
        pictures = this.pictures.toMutableList()
    )
}
//Template instantiation
fun instantiateTemplate(template: Template): Node {
    return Node(
        id = IdGenerator.newId(),
        title = template.name,
        icon = template.icon ?: Res.drawable.ic_folder,
        isContainer = true,
        children = template.defaultNodes.map { it.deepCopy() }
    )
}


//-----------------SUB TEMPLATES -----------------//
val hvacTemplate = Template(
    name = "HVAC System",
    icon = Res.drawable.ic_folder,
    defaultNodes = listOf(
        Node(
            id = "condenser",
            title = "Condenser",
            icon = Res.drawable.ic_docs,
            isContainer = false,
            fields = mutableListOf(
                Field(id = "year", label = "Year", type = FieldType.YEAR, fromTemplate = true),
                Field(id = "brand", label = "Brand", type = FieldType.TEXT, fromTemplate = true),
                Field(id = "model_number", label = "Model Number", type = FieldType.TEXT, fromTemplate = true),
                Field(id = "serial_number", label = "Serial Number", type = FieldType.TEXT, fromTemplate = true),
                Field(id = "tonnage", label = "Tonnage", type = FieldType.TEXT, fromTemplate = true)
            )
        ),
        Node(
            id = "furnace",
            title = "Furnace",
            icon = Res.drawable.ic_docs,
            isContainer = false,
            fields = mutableListOf(
                Field(id = "year", label = "Year", type = FieldType.YEAR, fromTemplate = true),
                Field(id = "brand", label = "Brand", type = FieldType.TEXT, fromTemplate = true),
                Field(id = "model_number", label = "Model Number", type = FieldType.TEXT, fromTemplate = true),
                Field(id = "serial_number", label = "Serial Number", type = FieldType.TEXT, fromTemplate = true),
                Field(id = "filter_size", label = "Filter Size(s)", type = FieldType.TEXT, fromTemplate = true)
            )
        ),
        Node(
            id = "indoor coil",
            title = "Indoor Coil",
            icon = Res.drawable.ic_docs,
            isContainer = false,
            fields = mutableListOf(
                Field(id = "year", label = "Year", type = FieldType.YEAR, fromTemplate = true),
                Field(id = "brand", label = "Brand", type = FieldType.TEXT, fromTemplate = true),
                Field(id = "model_number", label = "Model Number", type = FieldType.TEXT, fromTemplate = true),
                Field(id = "serial_number", label = "Serial Number", type = FieldType.TEXT, fromTemplate = true)
            )
        ),
        Node(
            id = "air handler",
            title = "Air Handler (no furnace)",
            icon = Res.drawable.ic_docs,
            isContainer = false,
            fields = mutableListOf(
                Field(id = "year", label = "Year", type = FieldType.YEAR, fromTemplate = true),
                Field(id = "brand", label = "Brand", type = FieldType.TEXT, fromTemplate = true),
                Field(id = "model_number", label = "Model Number", type = FieldType.TEXT, fromTemplate = true),
                Field(id = "serial_number", label = "Serial Number", type = FieldType.TEXT, fromTemplate = true),
                Field(id = "filter_size", label = "Filter Size(s)", type = FieldType.TEXT, fromTemplate = true)
            )
        )
    )
)

val vehicleFileTemplate = Template(
    name = "Vehicle",
    icon = Res.drawable.ic_docs,
    defaultNodes = listOf(
        Node(
            id = "vehicle_details",
            title = "Vehicle",
            icon = Res.drawable.ic_docs,
            isContainer = false,
            fields = mutableListOf(
                Field(id = "year", label = "Year", type = FieldType.YEAR, fromTemplate = true),
                Field(id = "make_model_trim", label = "Make/Model/Trim", type = FieldType.TEXT, fromTemplate = true),
                Field(id = "vin", label = "VIN", type = FieldType.TEXT, fromTemplate = true),
                Field(id = "oil_type", label = "Oil Type", type = FieldType.TEXT, fromTemplate = true),
            )
        )
    )
)

//-----------------MAIN TEMPLATES -----------------//
val homeTemplate = Template(
    name = "Home",
    icon = Res.drawable.ic_home,
    defaultNodes = listOf(
        Node(
            id = "hvac_category",
            title = "HVAC",
            icon = Res.drawable.ic_folder,
            isContainer = true,
            children = listOf(
                Node(
                    id = "hvac_system",
                    title = "HVAC System",
                    icon = Res.drawable.ic_folder,
                    isContainer = true,
                    children = instantiateTemplate(hvacTemplate).children
                )
            )
        )
    )
)

val vehicleFolderTemplate = Template(
    name = "Vehicle(s)",
    icon = Res.drawable.ic_directions_car,
    defaultNodes = listOf(
        Node(
            id = "vehicle_category",
            title = "Vehicle",
            icon = Res.drawable.ic_folder,
            isContainer = true,
            children = instantiateTemplate(vehicleFileTemplate).children
        )
    )
)

val allTemplates = listOf(
    homeTemplate,
    hvacTemplate,
    vehicleFolderTemplate,
    vehicleFileTemplate
)