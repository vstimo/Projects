library IEEE;
use IEEE.STD_LOGIC_1164.all;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity bistabil is
	port(d,clk,r: in std_logic;
	q:out std_logic);
end entity ;

architecture Bistabil_D of bistabil is
component divizorD is
	port(clk:in std_logic;
		clk_nou: out std_logic);
end component;
signal clk_bis: std_logic;
begin
D1: divizorD port map(clk,clk_bis);
	process(R,CLK) 
	begin
	if r='1' then q<='0';
	elsif (clk_bis='1' and clk_bis'event) then q<=d;
	end if;
	end process;
end architecture ;