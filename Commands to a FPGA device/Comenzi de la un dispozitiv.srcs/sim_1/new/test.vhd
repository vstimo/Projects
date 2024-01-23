library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity test is
    --  Port ( );
end test;

architecture Behavioral of test is
component RegSwitch is
    port(clk, rd, rst:in std_logic;
         data:in std_logic_vector(15 downto 0);
         flag:out std_logic;
         R_D:out std_logic_vector(7 downto 0));
end component;
    
    constant CLK_PERIOD: time := 10ns;
    signal clk, rst, rd, flag: std_logic;
    signal R_D: std_logic_vector(7 downto 0);
    signal data: std_logic_vector(15 downto 0);
begin
DUT: RegSwitch port map(clk, rd, rst, data, flag, R_D);

gen_clk: process
    begin
        clk <= '0';
        wait for (CLK_PERIOD/2);
        clk <= '1';
        wait for (CLK_PERIOD/2);
    end process gen_clk;
    
    process
    begin 
    rst<='1';
    wait for CLK_PERIOD;
    rst<='0';
    wait for CLK_PERIOD;
    rd<='1'; data<=x"FFAA";
    wait for CLK_PERIOD;
    rd<='0';
    wait for CLK_PERIOD;
    rd<='1'; data<=x"FFAA";
    wait for CLK_PERIOD;
    rd<='0';
    wait for CLK_PERIOD;
    rd<='1'; data<=x"FFAA";
    wait for CLK_PERIOD;
    rd<='0';
    wait for CLK_PERIOD;
    rd<='1'; data<=x"FFAA";
    wait;
    end process;

end Behavioral;
