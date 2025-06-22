package com.example.testapp.responses;

import com.example.testapp.models.Cart;
import com.example.testapp.models.Shipment;

import java.util.List;

public class ShipmentResponses {

    public class SingleShipmentResponse {
        private Shipment shipment;

        public Shipment getShipment() {
            return shipment;
        }

        public void setShipment(Shipment shipment) {
            this.shipment = shipment;
        }
    }

    public class MultiShipmentResponse {

        private List<Shipment> shipments;

        public List<Shipment> getShipments() {
            return shipments;
        }

        public void setShipments(List<Shipment> shipments) {
            this.shipments = shipments;
        }
    }
}
