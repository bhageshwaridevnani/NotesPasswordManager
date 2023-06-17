package com.example.demo.Base;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResponse<T> {


        private T DATA;
        private String MESSAGE;
        private int SUCCESS;

        public ApiResponse(T DATA, String MESSAGE, int SUCCESS) {
            this.DATA = DATA;
            this.MESSAGE = MESSAGE;
            this.SUCCESS = SUCCESS;
        }

        @JsonProperty("status")
        public int getSUCCESS() {
            return SUCCESS;
        }

        public void setSUCCESS(int SUCCESS) {
            this.SUCCESS = SUCCESS;
        }

        @JsonProperty("data")
        public T getDATA() {
            return DATA;
        }

        public void setDATA(T DATA) {
            this.DATA = DATA;
        }

        @JsonProperty("message")
        public String getMESSAGE() {
            return MESSAGE;
        }

        public void setMESSAGE(String MESSAGE) {
            this.MESSAGE = MESSAGE;
        }

    }
