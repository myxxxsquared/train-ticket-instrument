// package rebook.dto;
// import edu.fudan.common.entity.*;
// import rebook.entity.RebookInfo;

// public class OrderUpdateDto {
//     private Order order;
//     private RebookInfo rebookInfo;
//     private TripAllDetail tripAllDetail;
//     private String ticketPrice;
//     private String orderMoneyDifference;

//     public OrderUpdateDto(Order order, TripAllDetail tripAllDetail, String ticketPrice, String orderMoneyDifference, RebookInfo rebookInfo) {
//         this.order = order;
//         this.tripAllDetail = tripAllDetail;
//         this.ticketPrice = ticketPrice;
//         this.rebookInfo = rebookInfo;  // explicitly set to null
//         this.orderMoneyDifference = orderMoneyDifference;  // explicitly set to null
//     }
    
//     public Order getOrder() {
//         return order;
//     }

//     public RebookInfo getRebookInfo() {
//         return rebookInfo;
//     }

//     public TripAllDetail getTripAllDetail() {
//         return tripAllDetail;
//     }

//     public String getTicketPrice() {
//         return ticketPrice;
//     }

//     public String getorderMoneyDifference() {
//         return orderMoneyDifference;
//     }
//     // Getters and setters
// }

package rebook.dto;

import edu.fudan.common.entity.*;
import rebook.entity.RebookInfo;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateDto {
    private Order order;
    private RebookInfo rebookInfo;
    private TripAllDetail tripAllDetail;
    private String ticketPrice;
    private String orderMoneyDifference;
}
