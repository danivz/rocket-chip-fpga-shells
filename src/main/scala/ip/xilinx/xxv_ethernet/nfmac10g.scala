package sifive.fpgashells.ip.xilinx.xxv_ethernet

import chisel3._
import chisel3.util._
import org.chipsalliance.cde.config._
import freechips.rocketchip.diplomacy._

class nfmac10g extends BlackBox {
  val io = IO(new Bundle {
    val tx_clk0       = Input(Clock())
    val rx_clk0       = Input(Clock())
    val reset         = Input(Reset())
    val tx_dcm_locked = Input(Bool())
    val rx_dcm_locked = Input(Bool())

    // XGMII
    val xgmii_txd = Output(UInt(64.W))
    val xgmii_txc = Output(UInt(8.W))
    val xgmii_rxd = Input (UInt(64.W))
    val xgmii_rxc = Input (UInt(8.W))

    // Tx AXIS
    val tx_axis_aresetn = Input (Reset())
    val tx_axis_tready  = Output(Bool())
    val tx_axis_tvalid  = Input (Bool())
    val tx_axis_tlast   = Input (Bool())
    val tx_axis_tdata   = Input (UInt(64.W))
    val tx_axis_tkeep   = Input (UInt(8.W))
    val tx_axis_tuser   = Input (UInt(1.W))

    // Rx AXIS
    val rx_axis_aresetn = Input (Reset())
    val rx_axis_tvalid  = Output(Bool())
    val rx_axis_tlast   = Output(Bool())
    val rx_axis_tdata   = Output(UInt(64.W))
    val rx_axis_tkeep   = Output(UInt(8.W))
    val rx_axis_tuser   = Output(UInt(1.W))

    // Unused by nfmac10g
    val tx_ifg_delay = Input(UInt(8.W))
    val pause_val    = Input(UInt(16.W))
    val pause_req    = Input(Bool())
    val tx_configuration_vector = Input(UInt(80.W))
    val rx_configuration_vector = Input(UInt(80.W))
    val status_vector        = Output(UInt(2.W))
    val tx_statistics_vector = Output(UInt(26.W))
    val tx_statistics_valid  = Output(Bool())
    val rx_statistics_vector = Output(UInt(30.W))
    val rx_statistics_valid  = Output(Bool())
  })
}

/*
   Copyright 2016 SiFive, Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
