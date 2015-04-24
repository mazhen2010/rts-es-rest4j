package org.elasticsearch.search.builder;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: mazhen01
 * Date: 2015/4/24
 * Time: 21:39
 */
public class SearchSourceBuilderTest {

    @Test
    public void testJson() {
        QueryBuilder query = QueryBuilders.matchQuery("firmName", "清泉");
        FilterBuilder stationFilter = FilterBuilders.termFilter("stationId", 5);
        FilterBuilder staffFilter = FilterBuilders.termFilter("staffId", 936);
        query = QueryBuilders.filteredQuery(query, FilterBuilders.andFilter(stationFilter).add(staffFilter));
        SortBuilder dealSorter = SortBuilders.fieldSort("dealCount").order(SortOrder.ASC);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String json = searchSourceBuilder.query(query).toString();
        System.out.println(json);
    }
}
