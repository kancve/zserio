<#include "FileHeader.inc.ftl"/>
<#include "DocComment.inc.ftl">
<#if withTypeInfoCode>
    <#include "TypeInfo.inc.ftl"/>
</#if>
<@file_header generatorDescription/>
<@future_annotations/>
<@all_imports packageImports symbolImports typeImports/>
<#if withTypeInfoCode>
    <@package_imports ["zserio"]/>
</#if>
<#if withWriterCode>
    <#assign hasWithoutRowIdTable=false/>
    <#list fields as field>
        <#if field.isWithoutRowIdTable>
            <#assign hasWithoutRowIdTable=true/>
            <#break>
        </#if>
    </#list>
</#if>
<#macro table_name_constant field>
    TABLE_NAME_${field.snakeCaseName?upper_case}<#t>
</#macro>

class ${name}:
<#if withCodeComments && docComments??>
<@doc_comments docComments, 1/>

</#if>
    def __init__(self, connection: apsw.Connection, table_to_attached_db_name_relocation_map: typing.Dict[str, str] = None) -> None:
<#if withCodeComments>
        """
        Constructor from external connection and table relocation map.

        The method uses given external connection if database is already open and for each relocated table uses
        provided already attached database names.

        :param connection: Database connection of already open database.
        :param table_to_attached_db_name_relocation_map: Mapping of relocated table name to attached database file name.
        """

</#if>
        self._connection: apsw.Connection = connection
        self._attached_db_name_list: typing.Union[typing.List[str], typing.ValuesView[str]] = []
        self._is_external: bool = True
        self._init_tables(table_to_attached_db_name_relocation_map if table_to_attached_db_name_relocation_map else {})

    @classmethod
    def from_file(cls: typing.Type['${name}'], filename: str, table_to_db_filename_relocation_map: typing.Dict[str, str] = None) -> '${name}':
<#if withCodeComments>
        """
        Returns new object instance constructed from database file name and table relocation map.

        The method opens the database of given file name and for each relocated table attaches database to
        which relocated table should be mapped.

        :param filename: Database file name to use.
        :param table_to_db_filename_relocation_map: Mapping of relocated table name to database file name.
        """

</#if>
        connection = apsw.Connection(filename, apsw.SQLITE_OPEN_URI | <#rt>
            <#lt><#if withWriterCode>apsw.SQLITE_OPEN_READWRITE | apsw.SQLITE_OPEN_CREATE<#else>apsw.SQLITE_OPEN_READONLY</#if>)

        table_name_to_attached_db_name_map: typing.Dict[str, str] = {}
        db_filename_to_attached_db_name_map: typing.Dict[str, str] = {}
        if table_to_db_filename_relocation_map:
            cursor = connection.cursor()
            for relocated_table_name, db_filename in table_to_db_filename_relocation_map.items():
                attached_db_name = db_filename_to_attached_db_name_map.get(db_filename)
                if attached_db_name is None:
                    attached_db_name = cls.DATABASE_NAME + "_" + relocated_table_name
                    cls._attach_database(cursor, db_filename, attached_db_name)
                    db_filename_to_attached_db_name_map[db_filename] = attached_db_name

                table_name_to_attached_db_name_map[relocated_table_name] = attached_db_name

        instance = cls(connection, table_name_to_attached_db_name_map)
        instance._attached_db_name_list = db_filename_to_attached_db_name_map.values()
        instance._is_external = False

        return instance
<#if withTypeInfoCode>

    @staticmethod
    def type_info() -> zserio.typeinfo.TypeInfo:
    <#if withCodeComments>
        """
        Gets static information about the database type useful for generic introspection.

        :returns: Zserio type information.
        """

    </#if>
        table_list: typing.List[zserio.typeinfo.MemberInfo] = [
    <#list fields as field>
            <@member_info_database_field field field?has_next/>
    </#list>
        ]
        attribute_list = {
            zserio.typeinfo.TypeAttribute.TABLES : table_list
        }

        return zserio.typeinfo.TypeInfo('${schemaTypeFullName}', ${name}, attributes=attribute_list)
</#if>

    def close(self) -> None:
<#if withCodeComments>
        """
        Closes the database if it was constructed using file name (not using external connection).
        """

</#if>
        if not self._is_external:
            try:
                self._detach_databases()
            finally:
                self._connection.close()
        self._connection = None
<#list fields as field>

    <#macro field_member_name field>
        _${field.snakeCaseName}_<#t>
    </#macro>
    @property
    def ${field.propertyName}(self) -> ${field.typeInfo.typeFullName}:
    <#if withCodeComments>
        """
        Gets the table ${field.name}.

        <#if field.docComments??>
        **Description:**

        <@doc_comments_inner field.docComments, 2/>

        </#if>
        :returns: Reference to the table ${field.name}.
        """

    </#if>
        return self.<@field_member_name field/>
</#list>

    @property
    def connection(self) -> apsw.Connection:
<#if withCodeComments>
        """
        Gets the connection of underlying database.

        :returns: Database connection.
        """

</#if>
        return self._connection
<#if withWriterCode>

    def create_schema(self<#if hasWithoutRowIdTable>, without_rowid_table_names_blacklist: typing.List[str] = None</#if>) -> None:
    <#if withCodeComments>
        """
        Creates all tables in the database.
        <#if hasWithoutRowIdTable>

        :param without_rowid_table_names_blacklist: Set of rowid table names which should not be created.
        </#if>
        """

    </#if>
        has_autocommit = self._connection.getautocommit()
        if has_autocommit:
            cursor = self._connection.cursor()
            cursor.execute("BEGIN")
    <#if hasWithoutRowIdTable>

        if without_rowid_table_names_blacklist is None:
            without_rowid_table_names_blacklist = []
    </#if>

    <#list fields as field>
        <#if field.isWithoutRowIdTable>
        if self.<@table_name_constant field/> in without_rowid_table_names_blacklist:
            self.<@field_member_name field/>.create_ordinary_rowid_table()
        else:
            self.<@field_member_name field/>.create_table()
        <#else>
        self.<@field_member_name field/>.create_table()
        </#if>
    </#list>

        if has_autocommit:
            cursor.execute("COMMIT")

    def delete_schema(self) -> None:
    <#if withCodeComments>
        """
        Deletes all tables from the database.
        """

    </#if>
        has_autocommit = self._connection.getautocommit()
        if has_autocommit:
            cursor = self._connection.cursor()
            cursor.execute("BEGIN")

    <#list fields as field>
        self.<@field_member_name field/>.delete_table()
    </#list>

        if has_autocommit:
            cursor.execute("COMMIT")
</#if>

    def _init_tables(self, table_name_to_attached_db_name_map: typing.Dict[str, str]) -> None:
<#list fields as field>
        self.<@field_member_name field/> = ${field.typeInfo.typeFullName}(
            self._connection, self.<@table_name_constant field/>, table_name_to_attached_db_name_map.get(self.<@table_name_constant field/>))
</#list>

    @staticmethod
    def _attach_database(cursor: typing.Any, db_filename: str, db_name: str) -> None:
        sql_query = "ATTACH DATABASE '"
        sql_query += db_filename
        sql_query += "' AS "
        sql_query += db_name
        cursor.execute(sql_query)

    def _detach_databases(self) -> None:
        for attached_db_name in self._attached_db_name_list:
            sql_query = "DETACH DATABASE " + attached_db_name
            cursor = self._connection.cursor()
            cursor.execute(sql_query)

    DATABASE_NAME = "${name}"
<#list fields as field>
    <@table_name_constant field/> = "${field.name}"
</#list>
