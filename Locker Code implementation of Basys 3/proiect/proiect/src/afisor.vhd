library	 IEEE;
use IEEE.STD_LOGIC_1164.all;

entity hex7seg_top is
	port(mclk: in STD_LOGIC; --clockul de la procesor
	x: in STD_LOGIC_VECTOR(11 downto 0);
	a_to_g : out STD_LOGIC_VECTOR(6 downto 0);--catozi
	an : out STD_LOGIC_VECTOR(3 downto 0); --anozi
	pornire: in STD_LOGIC);
end hex7seg_top;

architecture  hex7seg_top of hex7seg_top is
component hex7seg is
	port(x: in STD_LOGIC_VECTOR(11 downto 0);
	clk : in STD_LOGIC;
	a_to_g: out STD_LOGIC_VECTOR(6 downto 0);
	an: out STD_LOGIC_VECTOR(3 downto 0);
	pornire: in STD_LOGIC);
end component;

begin
	X1: hex7seg port map(x,mclk,a_to_g,an,pornire);
end hex7seg_top;