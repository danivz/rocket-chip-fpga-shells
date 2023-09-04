package sifive.fpgashells.ip.xilinx.vc709mig

import Chisel._
import chisel3.experimental.{Analog,attach}
import freechips.rocketchip.util.{ElaborationArtefacts}
import freechips.rocketchip.util.GenericParameterizedBundle
import org.chipsalliance.cde.config._

// IP VLNV: xilinx.com:customize_ip:vc709mig:1.0
// Black Box

class VC709MIGIODDR(depth : BigInt) extends GenericParameterizedBundle(depth) {
  require((depth<=0x100000000L),"VC709MIGIODDR supports upto 4GB depth configuraton")
  val ddr3_addr             = Bits(OUTPUT,16)
  val ddr3_ba               = Bits(OUTPUT,3)
  val ddr3_ras_n            = Bool(OUTPUT)
  val ddr3_cas_n            = Bool(OUTPUT)
  val ddr3_we_n             = Bool(OUTPUT)
  val ddr3_reset_n          = Bool(OUTPUT)
  val ddr3_ck_p             = Bits(OUTPUT,1)
  val ddr3_ck_n             = Bits(OUTPUT,1)
  val ddr3_cke              = Bits(OUTPUT,1)
  val ddr3_cs_n             = Bits(OUTPUT,1)
  val ddr3_dm               = Bits(OUTPUT,8)
  val ddr3_odt              = Bits(OUTPUT,1)

  val ddr3_dq               = Analog(64.W)
  val ddr3_dqs_n            = Analog(8.W)
  val ddr3_dqs_p            = Analog(8.W)
}

//reused directly in io bundle for sifive.blocks.devices.xilinxvc709mig
trait VC709MIGIOClocksReset extends Bundle {
  //inputs
  //"NO_BUFFER" clock source (must be connected to IBUF outside of IP)
  val sys_clk_i             = Bool(INPUT)
  //user interface signals
  val ui_clk                = Clock(OUTPUT)
  val ui_clk_sync_rst       = Bool(OUTPUT)
  val mmcm_locked           = Bool(OUTPUT)
  val aresetn               = Bool(INPUT)
  //misc
  val init_calib_complete   = Bool(OUTPUT)
  val sys_rst               = Bool(INPUT)
}

//scalastyle:off
//turn off linter: blackbox name must match verilog module
class vc709mig(depth : BigInt)(implicit val p:Parameters) extends BlackBox
{
  require((depth<=0x100000000L),"vc709mig supports upto 4GB depth configuraton")
  override def desiredName = "vc709mig4gb"

  val io = new VC709MIGIODDR(depth) with VC709MIGIOClocksReset {
    // User interface signals
    val app_sr_req            = Bool(INPUT)
    val app_ref_req           = Bool(INPUT)
    val app_zq_req            = Bool(INPUT)
    val app_sr_active         = Bool(OUTPUT)
    val app_ref_ack           = Bool(OUTPUT)
    val app_zq_ack            = Bool(OUTPUT)
    //axi_s
    //slave interface write address ports
    val s_axi_awid            = Bits(INPUT,4)
    val s_axi_awaddr          = Bits(INPUT,32)
    val s_axi_awlen           = Bits(INPUT,8)
    val s_axi_awsize          = Bits(INPUT,3)
    val s_axi_awburst         = Bits(INPUT,2)
    val s_axi_awlock          = Bits(INPUT,1)
    val s_axi_awcache         = Bits(INPUT,4)
    val s_axi_awprot          = Bits(INPUT,3)
    val s_axi_awqos           = Bits(INPUT,4)
    val s_axi_awvalid         = Bool(INPUT)
    val s_axi_awready         = Bool(OUTPUT)
    //slave interface write data ports
    val s_axi_wdata           = Bits(INPUT,64)
    val s_axi_wstrb           = Bits(INPUT,8)
    val s_axi_wlast           = Bool(INPUT)
    val s_axi_wvalid          = Bool(INPUT)
    val s_axi_wready          = Bool(OUTPUT)
    //slave interface write response ports
    val s_axi_bready          = Bool(INPUT)
    val s_axi_bid             = Bits(OUTPUT,4)
    val s_axi_bresp           = Bits(OUTPUT,2)
    val s_axi_bvalid          = Bool(OUTPUT)
    //slave interface read address ports
    val s_axi_arid            = Bits(INPUT,4)
    val s_axi_araddr          = Bits(INPUT,32)
    val s_axi_arlen           = Bits(INPUT,8)
    val s_axi_arsize          = Bits(INPUT,3)
    val s_axi_arburst         = Bits(INPUT,2)
    val s_axi_arlock          = Bits(INPUT,1)
    val s_axi_arcache         = Bits(INPUT,4)
    val s_axi_arprot          = Bits(INPUT,3)
    val s_axi_arqos           = Bits(INPUT,4)
    val s_axi_arvalid         = Bool(INPUT)
    val s_axi_arready         = Bool(OUTPUT)
    //slave interface read data ports
    val s_axi_rready          = Bool(INPUT)
    val s_axi_rid             = Bits(OUTPUT,4)
    val s_axi_rdata           = Bits(OUTPUT,64)
    val s_axi_rresp           = Bits(OUTPUT,2)
    val s_axi_rlast           = Bool(OUTPUT)
    val s_axi_rvalid          = Bool(OUTPUT)
    //misc
    val device_temp           = Bits(OUTPUT,12)
  }

   val vc709mig4gbprj = """ {<?xml version='1.0' encoding='UTF-8'?>
<!-- IMPORTANT: This is an internal file that has been generated by the MIG software. Any direct editing or changes made to this file may result in unpredictable behavior or data corruption. It is strongly advised that users do not edit the contents of this file. Re-run the MIG GUI with the required settings if any of the options provided below need to be altered. -->
<Project NoOfControllers="1" >
    <ModuleName>vc709mig4gb</ModuleName>
    <dci_inouts_inputs>1</dci_inouts_inputs>
    <dci_inputs>1</dci_inputs>
    <Debug_En>OFF</Debug_En>
    <DataDepth_En>1024</DataDepth_En>
    <LowPower_En>ON</LowPower_En>
    <XADC_En>Enabled</XADC_En>
    <TargetFPGA>xc7vx690t-ffg1761/-2</TargetFPGA>
    <Version>4.2</Version>
    <SystemClock>No Buffer</SystemClock>
    <ReferenceClock>Use System Clock</ReferenceClock>
    <SysResetPolarity>ACTIVE HIGH</SysResetPolarity>
    <BankSelectionFlag>FALSE</BankSelectionFlag>
    <InternalVref>0</InternalVref>
    <dci_hr_inouts_inputs>50 Ohms</dci_hr_inouts_inputs>
    <dci_cascade>0</dci_cascade>
    <Controller number="0" >
        <MemoryDevice>DDR3_SDRAM/SODIMMs/MT8KTF51264HZ-1G9</MemoryDevice>
        <TimePeriod>1250</TimePeriod>
        <VccAuxIO>2.0V</VccAuxIO>
        <PHYRatio>4:1</PHYRatio>
        <InputClkFreq>200</InputClkFreq>
        <UIExtraClocks>1</UIExtraClocks>
        <MMCM_VCO>800</MMCM_VCO>
        <MMCMClkOut0> 1.000</MMCMClkOut0>
        <MMCMClkOut1>1</MMCMClkOut1>
        <MMCMClkOut2>1</MMCMClkOut2>
        <MMCMClkOut3>1</MMCMClkOut3>
        <MMCMClkOut4>1</MMCMClkOut4>
        <DataWidth>64</DataWidth>
        <DeepMemory>1</DeepMemory>
        <DataMask>1</DataMask>
        <ECC>Disabled</ECC>
        <Ordering>Normal</Ordering>
        <CustomPart>FALSE</CustomPart>
        <NewPartName></NewPartName>
        <RowAddress>16</RowAddress>
        <ColAddress>10</ColAddress>
        <BankAddress>3</BankAddress>
        <MemoryVoltage>1.5V</MemoryVoltage>
        <C0_MEM_SIZE>4294967296</C0_MEM_SIZE>
        <UserMemoryAddressMap>BANK_ROW_COLUMN</UserMemoryAddressMap>
        <PinSelection>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="A20" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_addr[0]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="B21" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_addr[10]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="B17" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_addr[11]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="A15" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_addr[12]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="A21" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_addr[13]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="F17" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_addr[14]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="E17" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_addr[15]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="B19" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_addr[1]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="C20" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_addr[2]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="A19" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_addr[3]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="A17" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_addr[4]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="A16" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_addr[5]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="D20" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_addr[6]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="C18" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_addr[7]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="D17" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_addr[8]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="C19" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_addr[9]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="D21" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_ba[0]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="C21" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_ba[1]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="D18" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_ba[2]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="K17" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_cas_n"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15" PADName="E18" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_ck_n[0]"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15" PADName="E19" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_ck_p[0]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="K19" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_cke[0]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="J17" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_cs_n[0]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="M13" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dm[0]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="K15" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dm[1]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="F12" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dm[2]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="A14" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dm[3]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="C23" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dm[4]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="D25" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dm[5]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="C31" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dm[6]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="F31" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dm[7]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="N14" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[0]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="H13" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[10]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="J13" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[11]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="L16" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[12]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="L15" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[13]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="H14" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[14]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="J15" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[15]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="E15" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[16]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="E13" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[17]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="F15" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[18]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="E14" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[19]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="N13" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[1]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="G13" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[20]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="G12" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[21]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="F14" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[22]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="G14" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[23]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="B14" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[24]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="C13" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[25]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="B16" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[26]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="D15" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[27]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="D13" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[28]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="E12" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[29]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="L14" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[2]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="C16" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[30]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="D16" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[31]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="A24" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[32]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="B23" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[33]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="B27" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[34]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="B26" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[35]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="A22" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[36]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="B22" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[37]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="A25" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[38]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="C24" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[39]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="M14" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[3]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="E24" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[40]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="D23" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[41]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="D26" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[42]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="C25" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[43]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="E23" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[44]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="D22" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[45]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="F22" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[46]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="E22" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[47]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="A30" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[48]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="D27" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[49]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="M12" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[4]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="A29" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[50]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="C28" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[51]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="D28" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[52]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="B31" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[53]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="A31" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[54]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="A32" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[55]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="E30" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[56]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="F29" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[57]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="F30" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[58]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="F27" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[59]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="N15" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[5]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="C30" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[60]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="E29" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[61]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="F26" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[62]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="D30" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[63]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="M11" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[6]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="L12" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[7]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="K14" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[8]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15_T_DCI" PADName="K13" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dq[9]"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15_T_DCI" PADName="M16" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dqs_n[0]"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15_T_DCI" PADName="J12" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dqs_n[1]"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15_T_DCI" PADName="G16" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dqs_n[2]"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15_T_DCI" PADName="C14" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dqs_n[3]"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15_T_DCI" PADName="A27" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dqs_n[4]"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15_T_DCI" PADName="E25" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dqs_n[5]"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15_T_DCI" PADName="B29" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dqs_n[6]"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15_T_DCI" PADName="E28" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dqs_n[7]"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15_T_DCI" PADName="N16" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dqs_p[0]"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15_T_DCI" PADName="K12" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dqs_p[1]"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15_T_DCI" PADName="H16" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dqs_p[2]"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15_T_DCI" PADName="C15" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dqs_p[3]"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15_T_DCI" PADName="A26" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dqs_p[4]"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15_T_DCI" PADName="F25" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dqs_p[5]"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15_T_DCI" PADName="B28" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dqs_p[6]"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15_T_DCI" PADName="E27" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_dqs_p[7]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="H20" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_odt[0]"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="E20" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_ras_n"/>
          <Pin IN_TERM="" IOSTANDARD="LVCMOS15" PADName="P18" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_reset_n"/>
          <Pin IN_TERM="" IOSTANDARD="SSTL15" PADName="F20" SLEW="FAST" VCCAUX_IO="HIGH" name="ddr3_we_n"/>
          <Pin IN_TERM="" IOSTANDARD="LVCMOS18" PADName="AM39" SLEW="" VCCAUX_IO="DONTCARE" name="init_calib_complete"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15" PADName="G18" SLEW="" VCCAUX_IO="DONTCARE" name="sys_clk_n"/>
          <Pin IN_TERM="" IOSTANDARD="DIFF_SSTL15" PADName="H19" SLEW="" VCCAUX_IO="DONTCARE" name="sys_clk_p"/>
        </PinSelection>
        <System_Control>
          <Pin Bank="15" PADName="AV40" name="sys_rst"/>
          <Pin Bank="Select Bank" PADName="No connect" name="init_calib_complete"/>
          <Pin Bank="Select Bank" PADName="No connect" name="tg_compare_error"/>
        </System_Control>
        <TimingParameters>
          <Parameters tcke="5" tfaw="27" tras="34" trcd="13.91" trefi="7.8" trfc="260" trp="13.91" trrd="5" trtp="7.5" twtr="7.5"/>
        </TimingParameters>
        <mrBurstLength name="Burst Length">8 - Fixed</mrBurstLength>
        <mrBurstType name="Read Burst Type and Length">Sequential</mrBurstType>
        <mrCasLatency name="CAS Latency">11</mrCasLatency>
        <mrMode name="Mode">Normal</mrMode>
        <mrDllReset name="DLL Reset">No</mrDllReset>
        <mrPdMode name="DLL control for precharge PD">Slow Exit</mrPdMode>
        <emrDllEnable name="DLL Enable">Enable</emrDllEnable>
        <emrOutputDriveStrength name="Output Driver Impedance Control">RZQ/6</emrOutputDriveStrength>
        <emrMirrorSelection name="Address Mirroring">Disable</emrMirrorSelection>
        <emrCSSelection name="Controller Chip Select Pin">Enable</emrCSSelection>
        <emrRTT name="RTT (nominal) - On Die Termination (ODT)">RZQ/4</emrRTT>
        <emrPosted name="Additive Latency (AL)">0</emrPosted>
        <emrOCD name="Write Leveling Enable">Disabled</emrOCD>
        <emrDQS name="TDQS enable">Enabled</emrDQS>
        <emrRDQS name="Qoff">Output Buffer Enabled</emrRDQS>
        <mr2PartialArraySelfRefresh name="Partial-Array Self Refresh">Full Array</mr2PartialArraySelfRefresh>
        <mr2CasWriteLatency name="CAS write latency">8</mr2CasWriteLatency>
        <mr2AutoSelfRefresh name="Auto Self Refresh">Enabled</mr2AutoSelfRefresh>
        <mr2SelfRefreshTempRange name="High Temparature Self Refresh Rate">Normal</mr2SelfRefreshTempRange>
        <mr2RTTWR name="RTT_WR - Dynamic On Die Termination (ODT)">Dynamic ODT off</mr2RTTWR>
        <PortInterface>AXI</PortInterface>
        <AXIParameters>
          <C0_C_RD_WR_ARB_ALGORITHM>RD_PRI_REG</C0_C_RD_WR_ARB_ALGORITHM>
          <C0_S_AXI_ADDR_WIDTH>32</C0_S_AXI_ADDR_WIDTH>
          <C0_S_AXI_DATA_WIDTH>64</C0_S_AXI_DATA_WIDTH>
          <C0_S_AXI_ID_WIDTH>4</C0_S_AXI_ID_WIDTH>
          <C0_S_AXI_SUPPORTS_NARROW_BURST>0</C0_S_AXI_SUPPORTS_NARROW_BURST>
        </AXIParameters>
      </Controller>

</Project>}"""

  val migprj = vc709mig4gbprj
  val migprjname =  """{/vc709mig4gb.prj}"""
  val modulename =  """vc709mig4gb"""


  ElaborationArtefacts.add(
  modulename++".vivado.tcl",
   """set migprj """++migprj++"""
   set migprjfile """++migprjname++"""
   set migprjfilepath $ipdir$migprjfile
   set fp [open $migprjfilepath w+]
   puts $fp $migprj
   close $fp
   create_ip -vendor xilinx.com -library ip -name mig_7series -module_name """ ++ modulename ++ """ -dir $ipdir -force
   set_property CONFIG.XML_INPUT_FILE $migprjfilepath [get_ips """ ++ modulename ++ """] """
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
