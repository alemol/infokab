package mx.geoint.database;

public class QueryTextProcess {
    static String query_main_base = " " +
            " SELECT (base.clave) as clave, base.codigo as codigos, concat('(',base.clave,')') as result_left, concat(base.descripcion,'[',base.clave,']') as result_right\n" +
            "    FROM bases as base\n" +
            "    where ? like concat(base.clave,'%')\n" +
            "    and length(?) <= length(base.clave)";

    static String query_main_base_with_one_suffixes = "" +
            " select concat(base1.clave,sufijo1.clave) as clave, concat(base1.codigos,'-',sufijo1.codigo) as codigos, concat(base1.result_left,'+',sufijo1.clave) as result_left, concat(base1.result_right,'+',sufijo1.descripcion) as result_right\n" +
            "    from sufijos as sufijo1, (\n" +
            "        SELECT base.clave as clave, base.codigo as base_code, base.codigo as codigos, concat('(',base.clave,')') as result_left, concat(base.descripcion,'[',base.clave,']') as result_right\n" +
            "        FROM bases as base\n" +
            "        where ? like concat(base.clave,'%')\n" +
            "        and length(?) >= length(base.clave)\n" +
            "    ) base1\n" +
            "    where ? like concat(base1.clave,sufijo1.clave,'%')\n" +
            "    and position(base1.base_code in sufijo1.codigo) > 0\n" +
            "    and length(?) <= length(concat(base1.clave,sufijo1.clave))";

    static String query_main_base_with_two_suffixes = "" +
            " select concat(base2.clave,sufijo2.clave) as clave, concat(base2.codigos,'-',sufijo2.codigo) as codigos, concat(base2.result_left,'+',sufijo2.clave) as result_left, concat(base2.result_right,'+',sufijo2.descripcion) as result_right\n" +
            "    from sufijos as sufijo2, (\n" +
            "        select concat(base1.clave,sufijo1.clave) as clave, base1.base_code as base_code, concat(base1.codigos,'-',sufijo1.codigo) as codigos, concat(base1.result_left,'+',sufijo1.clave) as result_left, concat(base1.result_right,'+',sufijo1.descripcion) as result_right\n" +
            "        from sufijos as sufijo1, (\n" +
            "            SELECT base.clave as clave, base.codigo as base_code, base.codigo as codigos, concat('(',base.clave,')') as result_left, concat(base.descripcion,'[',base.clave,']') as result_right\n" +
            "            FROM bases as base\n" +
            "            where ? like concat(base.clave,'%')\n" +
            "            and length(?) >= length(base.clave)\n" +
            "        ) base1\n" +
            "        where ? like concat(base1.clave,sufijo1.clave,'%')\n" +
            "        and position(base1.base_code in sufijo1.codigo) > 0\n" +
            "        and length(?) >= length(concat(base1.clave,sufijo1.clave))\n" +
            "    ) base2\n" +
            "    where ? like concat(base2.clave,sufijo2.clave,'%')\n" +
            "    and position(base2.base_code in sufijo2.codigo) > 0\n" +
            "    and length(?) <= length(concat(base2.clave,sufijo2.clave))";

    static String query_main_base_with_three_suffixes = " " +
            " select concat(base3.clave,sufijo3.clave) as clave, concat(base3.codigos,'-',sufijo3.codigo) as codigos, concat(base3.result_left,'+',sufijo3.clave) as result_left, concat(base3.result_right,'+',sufijo3.descripcion) as result_right\n" +
            "    from sufijos as sufijo3, (\n" +
            "        select concat(base2.clave,sufijo2.clave) as clave, base2.base_code as base_code, concat(base2.codigos,'-',sufijo2.codigo) as codigos, concat(base2.result_left,'+',sufijo2.clave) as result_left, concat(base2.result_right,'+',sufijo2.descripcion) as result_right\n" +
            "        from sufijos as sufijo2, (\n" +
            "            select concat(base1.clave,sufijo1.clave) as clave, base1.base_code as base_code, concat(base1.codigos,'-',sufijo1.codigo) as codigos, concat(base1.result_left,'+',sufijo1.clave) as result_left, concat(base1.result_right,'+',sufijo1.descripcion) as result_right\n" +
            "            from sufijos as sufijo1, (\n" +
            "                SELECT base.clave as clave, base.codigo as base_code, base.codigo as codigos, concat('(',base.clave,')') as result_left, concat(base.descripcion,'[',base.clave,']') as result_right\n" +
            "                FROM bases as base\n" +
            "                where ? like concat(base.clave,'%')\n" +
            "                and length(?) >= length(base.clave)\n" +
            "            ) base1\n" +
            "            where ? like concat(base1.clave,sufijo1.clave,'%')\n" +
            "            and position(base1.base_code in sufijo1.codigo) > 0\n" +
            "            and sufijo1.extra = ''\n" +
            "            and length(?) >= length(concat(base1.clave,sufijo1.clave))\n" +
            "        ) base2\n" +
            "        where ? like concat(base2.clave,sufijo2.clave,'%')\n" +
            "        and position(base2.base_code in sufijo2.codigo) > 0\n" +
            "        and sufijo2.extra = ''\n" +
            "        and length(?) >= length(concat(base2.clave,sufijo2.clave))\n" +
            "    ) base3\n" +
            "    where ? like concat(base3.clave,sufijo3.clave,'%')\n" +
            "    and position(base3.base_code in sufijo3.codigo) > 0\n" +
            "    and length(?) <= length(concat(base3.clave,sufijo3.clave))";

    static String query_main_base_with_four_suffixes = " " +
            " select concat(base4.clave,sufijo4.clave) as clave, concat(base4.codigos,'-',sufijo4.codigo) as codigos, concat(base4.result_left,'+',sufijo4.clave) as result_left, concat(base4.result_right,'+',sufijo4.descripcion) as result_right\n" +
            "    from sufijos as sufijo4, (\n" +
            "        select concat(base3.clave,sufijo3.clave) as clave, base3.base_code as base_code, concat(base3.codigos,'-',sufijo3.codigo) as codigos, concat(base3.result_left,'+',sufijo3.clave) as result_left, concat(base3.result_right,'+',sufijo3.descripcion) as result_right\n" +
            "        from sufijos as sufijo3, (\n" +
            "            select concat(base2.clave,sufijo2.clave) as clave, base2.base_code as base_code, concat(base2.codigos,'-',sufijo2.codigo) as codigos, concat(base2.result_left,'+',sufijo2.clave) as result_left, concat(base2.result_right,'+',sufijo2.descripcion) as result_right\n" +
            "            from sufijos as sufijo2, (\n" +
            "                select concat(base1.clave,sufijo1.clave) as clave, base1.base_code as base_code, concat(base1.codigos,'-',sufijo1.codigo) as codigos, concat(base1.result_left,'+',sufijo1.clave) as result_left, concat(base1.result_right,'+',sufijo1.descripcion) as result_right\n" +
            "                from sufijos as sufijo1, (\n" +
            "                    SELECT base.clave as clave, base.codigo as base_code, base.codigo as codigos, concat('(',base.clave,')') as result_left, concat(base.descripcion,'[',base.clave,']') as result_right\n" +
            "                    FROM bases as base\n" +
            "                    where ? like concat(base.clave,'%')\n" +
            "                    and length(?) >= length(base.clave)\n" +
            "                ) base1\n" +
            "                where ? like concat(base1.clave,sufijo1.clave,'%')\n" +
            "                and position(base1.base_code in sufijo1.codigo) > 0\n" +
            "                and sufijo1.extra = ''\n" +
            "                and length(?) >= length(concat(base1.clave,sufijo1.clave))\n" +
            "            ) base2\n" +
            "            where ? like concat(base2.clave,sufijo2.clave,'%')\n" +
            "            and position(base2.base_code in sufijo2.codigo) > 0\n" +
            "            and sufijo2.extra = ''\n" +
            "            and length(?) >= length(concat(base2.clave,sufijo2.clave))\n" +
            "        ) base3\n" +
            "        where ? like concat(base3.clave,sufijo3.clave,'%')\n" +
            "        and position(base3.base_code in sufijo3.codigo) > 0\n" +
            "        and sufijo3.extra = ''\n" +
            "        and length(?) >= length(concat(base3.clave,sufijo3.clave))\n" +
            "    ) base4\n" +
            "    where ? like concat(base4.clave,sufijo4.clave,'%')\n" +
            "    and position(base4.base_code in sufijo4.codigo) > 0\n" +
            "    and length(?) <= length(concat(base4.clave,sufijo4.clave))";

    static String query_main_prefixes = " " +
            " select p.clave as clave, p.codigo as codigos, p.clave as result_left, p.descripcion as result_right\n" +
            "    from prefijos p \n" +
            "    where ? like concat(p.clave,'%')\n" +
            "    and length(?) <= length(p.clave)";

    static String query_main_prefixes_with_base = "" +
            " select concat(prefijo.clave,base.clave) as clave, concat(prefijo.codigos,'-',base.codigo) as codigos, concat(prefijo.result_left,'+','(',base.clave,')') as result_left, concat(prefijo.result_right,'+',base.descripcion) as result_right\n" +
            "    FROM bases as base, (\n" +
            "        select p.clave as clave, p.codigo as codigos, p.clave as result_left, p.descripcion as result_right\n" +
            "        from prefijos p \n" +
            "        where ? like concat(p.clave,'%')\n" +
            "        and length(?) >= length(p.clave)\n" +
            "    ) as prefijo\n" +
            "    where ? like concat(prefijo.clave,base.clave,'%')\n" +
            "    and position(base.codigo in prefijo.codigos) > 0\n" +
            "    and length(?) <= length(base.clave)";

    static String query_main_prefixes_with_base_with_one_suffixes = " " +
            " select concat(base1.clave,sufijo1.clave) as clave, concat(base1.codigos,'-',sufijo1.codigo) as codigos, concat(base1.result_left,'+',sufijo1.clave) as result_left, concat(base1.result_right,'+',sufijo1.descripcion) as result_right\n" +
            "    from sufijos as sufijo1, (\n" +
            "        select concat(prefijo.clave,base.clave) as clave, base.codigo as base_code, concat(prefijo.codigos,'-',base.codigo) as codigos, concat(prefijo.result_left,'+','(',base.clave,')') as result_left, concat(prefijo.result_right,'+',base.descripcion) as result_right\n" +
            "        FROM bases as base, (\n" +
            "            select p.clave as clave, p.codigo as codigos, p.clave as result_left, p.descripcion as result_right\n" +
            "            from prefijos p \n" +
            "            where ? like concat(p.clave,'%')\n" +
            "            and length(?) >= length(p.clave)\n" +
            "        ) as prefijo\n" +
            "        where ? like concat(prefijo.clave,base.clave,'%')\n" +
            "        and position(base.codigo in prefijo.codigos) > 0\n" +
            "        and length(?) >= length(base.clave)\n" +
            "    ) base1\n" +
            "    where ? like concat(base1.clave,sufijo1.clave,'%')\n" +
            "    and position(base1.base_code in sufijo1.codigo) > 0\n" +
            "    and length(?) <= length(concat(base1.clave,sufijo1.clave))";

    static String query_main_prefixes_with_base_with_two_suffixes = "" +
            " select concat(base2.clave,sufijo2.clave) as clave, concat(base2.codigos,'-',sufijo2.codigo) as codigos, concat(base2.result_left,'+',sufijo2.clave) as result_left, concat(base2.result_right,'+',sufijo2.descripcion) as result_right\n" +
            "    from sufijos as sufijo2, (\n" +
            "        select concat(base1.clave,sufijo1.clave) as clave, base1.base_code as base_code, concat(base1.codigos,'-',sufijo1.codigo) as codigos, concat(base1.result_left,'+',sufijo1.clave) as result_left, concat(base1.result_right,'+',sufijo1.descripcion) as result_right\n" +
            "        from sufijos as sufijo1, (\n" +
            "            select concat(prefijo.clave,base.clave) as clave, base.codigo as base_code, concat(prefijo.codigos,'-',base.codigo) as codigos, concat(prefijo.result_left,'+','(',base.clave,')') as result_left, concat(prefijo.result_right,'+',base.descripcion) as result_right\n" +
            "            FROM bases as base, (\n" +
            "                select p.clave as clave, p.codigo as codigos, p.clave as result_left, p.descripcion as result_right\n" +
            "                from prefijos p \n" +
            "                where ? like concat(p.clave,'%')\n" +
            "                and length(?) >= length(p.clave)\n" +
            "            ) as prefijo\n" +
            "            where ? like concat(prefijo.clave,base.clave,'%')\n" +
            "            and position(base.codigo in prefijo.codigos) > 0\n" +
            "            and length(?) >= length(base.clave)\n" +
            "        ) base1\n" +
            "        where ? like concat(base1.clave,sufijo1.clave,'%')\n" +
            "        and position(base1.base_code in sufijo1.codigo) > 0\n" +
            "        and length(?) >= length(concat(base1.clave,sufijo1.clave))\n" +
            "    ) base2\n" +
            "    where ? like concat(base2.clave,sufijo2.clave,'%')\n" +
            "    and position(base2.base_code in sufijo2.codigo) > 0\n" +
            "    and length(?) <= length(concat(base2.clave,sufijo2.clave))";

    static String query_main_prefixes_with_base_with_three_suffixes = "" +
            " select concat(base3.clave,sufijo3.clave) as clave, concat(base3.codigos,'-',sufijo3.codigo) as codigos, concat(base3.result_left,'+',sufijo3.clave) as result_left, concat(base3.result_right,'+',sufijo3.descripcion) as result_right\n" +
            "    from sufijos as sufijo3, (\n" +
            "        select concat(base2.clave,sufijo2.clave) as clave, base2.base_code as base_code, concat(base2.codigos,'-',sufijo2.codigo) as codigos, concat(base2.result_left,'+',sufijo2.clave) as result_left, concat(base2.result_right,'+',sufijo2.descripcion) as result_right\n" +
            "        from sufijos as sufijo2, (\n" +
            "            select concat(base1.clave,sufijo1.clave) as clave, base1.base_code as base_code, concat(base1.codigos,'-',sufijo1.codigo) as codigos, concat(base1.result_left,'+',sufijo1.clave) as result_left, concat(base1.result_right,'+',sufijo1.descripcion) as result_right\n" +
            "            from sufijos as sufijo1, (\n" +
            "                select concat(prefijo.clave,base.clave) as clave, base.codigo as base_code, concat(prefijo.codigos,'-',base.codigo) as codigos, concat(prefijo.result_left,'+','(',base.clave,')') as result_left, concat(prefijo.result_right,'+',base.descripcion) as result_right\n" +
            "                FROM bases as base, (\n" +
            "                    select p.clave as clave, p.codigo as codigos, p.clave as result_left, p.descripcion as result_right\n" +
            "                    from prefijos p \n" +
            "                    where ? like concat(p.clave,'%')\n" +
            "                    and length(?) >= length(p.clave)\n" +
            "                ) as prefijo\n" +
            "                where ? like concat(prefijo.clave,base.clave,'%')\n" +
            "                and position(base.codigo in prefijo.codigos) > 0\n" +
            "                and length(?) >= length(base.clave)\n" +
            "            ) base1\n" +
            "            where ? like concat(base1.clave,sufijo1.clave,'%')\n" +
            "            and position(base1.base_code in sufijo1.codigo) > 0\n" +
            "            and length(?) >= length(concat(base1.clave,sufijo1.clave))\n" +
            "        ) base2\n" +
            "        where ? like concat(base2.clave,sufijo2.clave,'%')\n" +
            "        and position(base2.base_code in sufijo2.codigo) > 0\n" +
            "        and length(?) >= length(concat(base2.clave,sufijo2.clave))\n" +
            "    ) base3\n" +
            "    where ? like concat(base3.clave,sufijo3.clave,'%')\n" +
            "    and position(base3.base_code in sufijo3.codigo) > 0\n" +
            "    and length(?) <= length(concat(base3.clave,sufijo3.clave))";

    public static String getQuery(){
        String query =
                "select clave, concat(result_left,' = ', result_right) as result " +
                "from (" +
                    query_main_base +
                    "union \n" + query_main_base_with_one_suffixes +
                    "union \n" + query_main_base_with_two_suffixes +
                    "union \n" + query_main_base_with_three_suffixes +
                    "union \n" + query_main_base_with_four_suffixes +
                    "union \n" + query_main_prefixes +
                    "union \n" + query_main_prefixes_with_base +
                    "union \n" + query_main_prefixes_with_base_with_one_suffixes +
                    "union \n" + query_main_prefixes_with_base_with_two_suffixes +
                    "union \n" + query_main_prefixes_with_base_with_three_suffixes +
                ") as t1";

        return query;
    }
}
