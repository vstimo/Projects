

library IEEE;
use IEEE.STD_LOGIC_1164.ALL;


entity tb is
    --  Port ( );
end tb;

architecture Behavioral of tb is
    component AnimationBlock is
        port(clk, rst:in std_logic;
             ascii:in std_logic_vector(7 downto 0);
             outleds:out std_logic_vector(15 downto 0));
    end component;
    constant clk_period : time := 20 ns;
    signal clk, rst: std_logic;
    signal outleds: std_logic_vector(15 downto 0);
begin
clk_process :process
    begin
        Clk <= '0';
        wait for clk_period/2;
        Clk <= '1';
        wait for clk_period/2;
    end process;
    
    dut: AnimationBlock port map(clk, rst, x"FF", outleds);
    
    stim_proc: process
    begin
        rst<='1';
        wait for 20 ns;
        rst<='0';
        wait;
    end process;

end Behavioral;
