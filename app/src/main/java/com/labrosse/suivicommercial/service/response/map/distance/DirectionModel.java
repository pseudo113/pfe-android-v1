package com.labrosse.suivicommercial.service.response.map.distance;

import java.util.List;

/**
 * Created by ahmedhammami on 22/02/16.
 */
public class DirectionModel {

    private String status;
    private List<String> destination_addresses;
    private List<String> origin_addresses;
    private List<RowsEntity> rows;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getDestination_addresses() {
        return destination_addresses;
    }

    public void setDestination_addresses(List<String> destination_addresses) {
        this.destination_addresses = destination_addresses;
    }

    public List<String> getOrigin_addresses() {
        return origin_addresses;
    }

    public void setOrigin_addresses(List<String> origin_addresses) {
        this.origin_addresses = origin_addresses;
    }

    public List<RowsEntity> getRows() {
        return rows;
    }

    public void setRows(List<RowsEntity> rows) {
        this.rows = rows;
    }

    public int getClosetsLocation() {

        int index = -1;
        if (getStatus().equalsIgnoreCase("ok")) {
            int minDistance = -1;


            for (RowsEntity rowsEntity : rows) {

                for (RowsEntity.ElementsEntity elementsEntity : rowsEntity.getElements()) {

                    if (elementsEntity.getStatus().equalsIgnoreCase("ok")) {

                        int newDistance = elementsEntity.getDistance().getValue();

                        if(minDistance == -1 || newDistance <= minDistance){
                            minDistance = newDistance;
                            index = rowsEntity.getElements().indexOf(elementsEntity);
                        }
                    }
                }
            }
        }

        return index;
    }

    public static class RowsEntity {
        /**
         * distance : {"text":"55 m","value":55}
         * duration : {"text":"1     minute","value":3}
         * status : OK
         */

        private List<ElementsEntity> elements;

        public List<ElementsEntity> getElements() {
            return elements;
        }

        public void setElements(List<ElementsEntity> elements) {
            this.elements = elements;
        }

        public static class ElementsEntity {
            /**
             * text : 55 m
             * value : 55
             */

            private DistanceEntity distance;
            /**
             * text : 1 minute
             * value : 3
             */

            private DurationEntity duration;
            private String status;

            public DistanceEntity getDistance() {
                return distance;
            }

            public void setDistance(DistanceEntity distance) {
                this.distance = distance;
            }

            public DurationEntity getDuration() {
                return duration;
            }

            public void setDuration(DurationEntity duration) {
                this.duration = duration;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public static class DistanceEntity {
                private String text;
                private int value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }

            public static class DurationEntity {
                private String text;
                private int value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }
        }
    }

}
