package com.location.mapper;

import com.location.model.Location;
import com.product.avalability.LocationDetails;

public interface LocationMapper {

    LocationDetails toLocationDoc(Location location);
}
