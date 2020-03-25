package com.lykke.api.testing.queryparams.model.b;

import com.lykke.api.testing.annotations.PublicApi;
import com.lykke.api.testing.annotations.QueryParameters;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
@PublicApi
@QueryParameters
public class PagedRequestModel {

    private int pageSize;
    private int currentPage;
}
