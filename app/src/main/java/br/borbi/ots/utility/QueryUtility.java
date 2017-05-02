package br.borbi.ots.utility;

import br.borbi.ots.data.OTSContract;

/**
 * Created by Gabriela on 15/06/2015.
 */
class QueryUtility {

    public static String buildQuerySelectSearchByCoordinatesAndDate(){
        StringBuilder whereClause = new StringBuilder(
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

    public static String buildQuerySelectSearchByDate(){
        StringBuilder whereClause = new StringBuilder(OTSContract.Search.COLUMN_NAME_DATE_END).append(" >= ?");
        return whereClause.toString();
    }
}
