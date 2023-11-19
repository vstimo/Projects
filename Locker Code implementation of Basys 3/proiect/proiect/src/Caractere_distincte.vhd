library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity carac_dis is
	port (reg1, reg2, reg3: in STD_LOGIC_VECTOR(3 downto 0);
	caractere_distincte: out STD_LOGIC);
end entity;

architecture carac_dis of carac_dis is
begin 
	process(reg1,reg2,reg3)
	begin
			if(reg1/=reg2 and reg1/=reg3 and reg2/=reg3) then
			caractere_distincte<='1';
			else  caractere_distincte<='0';
			end if;
	end process;
end architecture ;