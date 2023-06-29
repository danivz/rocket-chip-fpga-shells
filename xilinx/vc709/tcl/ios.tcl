#---------------Physical Constraints-----------------

set_property BOARD_PIN {clk_p} [get_ports sys_diff_clock_clk_p]
set_property BOARD_PIN {clk_n} [get_ports sys_diff_clock_clk_n]
set_property BOARD_PIN {reset} [get_ports reset]

create_clock -name sys_diff_clk -period 5.0 [get_ports sys_diff_clock_clk_p]
set_input_jitter [get_clocks -of_objects [get_ports sys_diff_clock_clk_p]] 0.5

set_property BOARD_PIN {leds_8bits_tri_o_0} [get_ports led_0]
set_property BOARD_PIN {leds_8bits_tri_o_1} [get_ports led_1]
set_property BOARD_PIN {leds_8bits_tri_o_2} [get_ports led_2]
set_property BOARD_PIN {leds_8bits_tri_o_3} [get_ports led_3]
set_property BOARD_PIN {leds_8bits_tri_o_4} [get_ports led_4]
set_property BOARD_PIN {leds_8bits_tri_o_5} [get_ports led_5]
set_property BOARD_PIN {leds_8bits_tri_o_6} [get_ports led_6]
set_property BOARD_PIN {leds_8bits_tri_o_7} [get_ports led_7]

set_property BOARD_PIN {push_buttons_5bits_tri_i_0}  [get_ports btn_0]
set_property BOARD_PIN {push_buttons_5bits_tri_i_1}  [get_ports btn_1]
set_property BOARD_PIN {push_buttons_5bits_tri_i_2}  [get_ports btn_2]
set_property BOARD_PIN {push_buttons_5bits_tri_i_3}  [get_ports btn_3]

set_property BOARD_PIN {dip_switches_tri_i_0} [get_ports sw_0]
set_property BOARD_PIN {dip_switches_tri_i_1} [get_ports sw_1]
set_property BOARD_PIN {dip_switches_tri_i_2} [get_ports sw_2]
set_property BOARD_PIN {dip_switches_tri_i_3} [get_ports sw_3]
set_property BOARD_PIN {dip_switches_tri_i_4} [get_ports sw_4]
set_property BOARD_PIN {dip_switches_tri_i_5} [get_ports sw_5]
set_property BOARD_PIN {dip_switches_tri_i_6} [get_ports sw_6]
set_property BOARD_PIN {dip_switches_tri_i_7} [get_ports sw_7]

set_property PACKAGE_PIN AU33 [get_ports uart_rx]
set_property IOSTANDARD LVCMOS18 [get_ports uart_rx]
set_property IOB TRUE [get_cells -of_objects [all_fanout -flat -endpoints_only [get_ports uart_rx]]]
set_property PACKAGE_PIN AT32 [get_ports uart_ctsn]
set_property IOSTANDARD LVCMOS18 [get_ports uart_ctsn]
set_property IOB TRUE [get_ports uart_ctsn]
set_property PACKAGE_PIN AU36 [get_ports uart_tx]
set_property IOSTANDARD LVCMOS18 [get_ports uart_tx]
set_property IOB TRUE  [get_cells -of_objects [all_fanin -flat -startpoints_only [get_ports uart_tx]]]
set_property PACKAGE_PIN AR34 [get_ports uart_rtsn]
set_property IOSTANDARD LVCMOS18 [get_ports uart_rtsn]
set_property IOB TRUE [get_ports uart_rtsn]
