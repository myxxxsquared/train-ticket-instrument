package rebook.service;

import edu.fudan.common.util.Response;
import org.springframework.http.HttpHeaders;
import rebook.entity.RebookInfo;
import edu.fudan.common.entity.*;
import rebook.dto.OrderUpdateDto;

/**
 * @author fdse
 */
public interface RebookService {
    Response rebook(RebookInfo info, HttpHeaders headers);
    Response payDifferentMoney(String orderId, String tripId, String userId, String money, HttpHeaders httpHeaders);
    Response updateOrder(Order order, RebookInfo info, TripAllDetail gtdr, String ticketPrice, HttpHeaders httpHeaders);
}
