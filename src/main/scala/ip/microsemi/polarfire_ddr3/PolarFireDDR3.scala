package sifive.fpgashells.ip.microsemi.polarfireddr3

import chisel3._
import chisel3.experimental.{Analog,attach}
import freechips.rocketchip.util.{ElaborationArtefacts}
import freechips.rocketchip.util.GenericParameterizedBundle
import org.chipsalliance.cde.config._

// Black Box for Microsemi PolarFire DDR3 controller version 2.1.101

class PolarFireEvalKitDDR3IODDR(depth : BigInt) extends GenericParameterizedBundle(depth) {

  val A                     = Output(Bits(16.W))
  val BA                    = Output(Bits(3.W))
  val RAS_N                 = Output(Bool())
  val CAS_N                 = Output(Bool())
  val WE_N                  = Output(Bool())
  val CTRLR_READY           = Output(Bool())
  val SHIELD0               = Output(Bool())
  val SHIELD1               = Output(Bool())
  val CK0                   = Output(Bits(1.W))
  val CK0_N                 = Output(Bits(1.W))
  val CKE                   = Output(Bits(1.W))
  val CS_N                  = Output(Bits(1.W))
  val DM                    = Output(Bits(2.W))
  val ODT                   = Output(Bits(1.W))
  val RESET_N               = Output(Bool())
  
  val DQ                    = Analog(16.W)
  val DQS                   = Analog(2.W)
  val DQS_N                 = Analog(2.W)
}

trait PolarFireEvalKitDDR3IOClocksReset extends Bundle {

  val SYS_RESET_N           = Input(Bool())
  val PLL_REF_CLK           = Input(Clock())  
  
  val SYS_CLK               = Output(Clock())
  val PLL_LOCK              = Output(Bool())
  
}

//scalastyle:off
//turn off linter: blackbox name must match verilog module
class DDR3_Subsys(depth : BigInt)(implicit val p:Parameters) extends BlackBox
{
  override def desiredName = "pf_ddr"

  val io = new PolarFireEvalKitDDR3IODDR(depth) with PolarFireEvalKitDDR3IOClocksReset {
    //axi slave interface
    //slave interface write address ports
    val axi0_awid             = Input(Bits(4.W))
    val axi0_awaddr           = Input(Bits(32.W))
    val axi0_awlen            = Input(Bits(8.W))
    val axi0_awsize           = Input(Bits(3.W))
    val axi0_awburst          = Input(Bits(2.W))
    val axi0_awlock           = Input(Bits(2.W))
    val axi0_awcache          = Input(Bits(4.W))
    val axi0_awprot           = Input(Bits(3.W))
    val axi0_awvalid          = Input(Bool())
    val axi0_awready          = Output(Bool())
    //slave interface write data ports
    val axi0_wdata            = Input(Bits(64.W))
    val axi0_wstrb            = Input(Bits(8.W))
    val axi0_wlast            = Input(Bool())
    val axi0_wvalid           = Input(Bool())
    val axi0_wready           = Output(Bool())
    //slave interface write response ports
    val axi0_bready           = Input(Bool())
    val axi0_bid              = Output(Bits(4.W))
    val axi0_bresp            = Output(Bits(2.W))
    val axi0_bvalid           = Output(Bool())
    //slave interface read address ports
    val axi0_arid             = Input(Bits(4.W))
    val axi0_araddr           = Input(Bits(32.W))
    val axi0_arlen            = Input(Bits(8.W))
    val axi0_arsize           = Input(Bits(3.W))
    val axi0_arburst          = Input(Bits(2.W))
    val axi0_arlock           = Input(Bits(2.W))
    val axi0_arcache          = Input(Bits(4.W))
    val axi0_arprot           = Input(Bits(3.W))
    val axi0_arvalid          = Input(Bool())
    val axi0_arready          = Output(Bool())
    //slave interface read data ports
    val axi0_rready           = Input(Bool())
    val axi0_rid              = Output(Bits(4.W))
    val axi0_rdata            = Output(Bits(64.W))
    val axi0_rresp            = Output(Bits(2.W))
    val axi0_rlast            = Output(Bool())
    val axi0_rvalid           = Output(Bool())
    //misc
    val AXI0_AWUSERTAG        = Input(Bits(4.W))
    val AXI0_BUSERTAG         = Output(Bits(4.W))
  }

  ElaborationArtefacts.add(
    "AddIPInstance.polarfire_ddr3.libero.tcl",
    """ 
create_design -id Actel:SystemBuilder:PF_DDR3:2.2.109 -design_name {pf_ddr} -config_file {} -params {} -inhibit_configurator 0
open_smartdesign -design pf_ddr
sysbld_configure_page -component pf_ddr -page PF_DDR3_UI -param WIDTH:16 \
                                                         -param CLOCK_DDR:666.666 \
                                                         -param CLOCK_PLL_REFERENCE:111.111 \
                                                         -param CCC_PLL_CLOCK_MULTIPLIER:6 \
                                                         -param ROW_ADDR_WIDTH:16 \
                                                         -param CAS_LATENCY:9 \
                                                         -param RTT_NOM:RZQ6 \
                                                         -param CAS_WRITE_LATENCY:7 \
                                                         -param OUTPUT_DRIVE_STRENGTH:RZQ7 \
                                                         -param TIMING_RAS:36 \
                                                         -param TIMING_RCD:13.5 \
                                                         -param TIMING_RP:13.5 \
                                                         -param TIMING_RC:49.5 \
                                                         -param TIMING_WR:15 \
                                                         -param TIMING_FAW:30 \
                                                         -param TIMING_WTR:5 \
                                                         -param TIMING_RRD:7.5 \
                                                         -param TIMING_RTP:7.5 \
                                                         -param TIMING_RFC:350 \
                                                         -param AXI_ID_WIDTH:6
save_design -component pf_ddr  -library {} -file {}
generate_design -component pf_ddr  -library {} -file {} -generator {} -recursive 1
close_design -component pf_ddr
"""
  )
}
//scalastyle:on

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
