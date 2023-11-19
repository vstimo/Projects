library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity comparator_cifre is
	port(reg1, reg2, reg3, reg4, reg5, reg6: in std_logic_vector(3 downto 0);
		comparator_cifre: out std_logic);
end entity;

architecture arh of comparator_cifre is
begin
	process(reg1,reg2,reg3,reg4,reg5,reg6)
	begin
		if(reg1=reg4 and reg2=reg5 and reg3=reg6) then
			comparator_cifre<='1';
		else
			comparator_cifre<='0';
		end if;
	end process;
end architecture;