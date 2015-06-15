package br.borbi.ots.utility;

import br.borbi.ots.data.OTSContract;

/**
 * Created by Gabriela on 15/06/2015.
 */
public class QueryUtility {

    public static String buildQuerySelectSearchByCoordinatesAndDate(){
        StringBuffer whereClause = new StringBuffer(
                OTSContract.Search.COLUMN_NAME_ORIGIN_LAT).append(" >= ?")
                .append(" AND ")
                .append(OTSContract.Search.COLUMN_NAME_ORIGIN_LAT).append(" <= ?")
                .append(" AND ")
                .append(OTSContract.Search.COLUMN_NAME_ORIGIN_LONG).append(" >= ?")
                .append(" AND ")
                .append(OTSContract.Search.COLUMN_NAME_ORIGIN_LONG).append(" <= ?")
                .append(" AND ")
                .append(OTSContract.Search.COLUMN_NAME_DATE_END).append(" >= ?");

        return whereClause.toString();
    }
}
