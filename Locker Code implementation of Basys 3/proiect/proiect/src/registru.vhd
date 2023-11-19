library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.all;
use IEEE.STD_LOGIC_UNSIGNED.all;

entity registru is 
	port(abcd   : in STD_LOGIC_VECTOR(3 downto 0);
    	load  : in STD_LOGIC; 
    	reset: in STD_LOGIC; 
    	clk : in STD_LOGIC; 
    	Q   : out STD_LOGIC_VECTOR(3 downto 0):="0000"
);
end registru;

architecture registru of registru is
signal temp: STD_LOGIC_VECTOR(3 downto 0):="0000";
begin
    process(clk, reset,load)
    begin 
        if reset = '1' then
            temp <= "0000";
        elsif clk='1' and clk'event then
            if load = '1' then
                temp <= abcd;
			else temp<=temp;
            end if;
        end if;
	Q<=temp;
    end process;
end architecture;