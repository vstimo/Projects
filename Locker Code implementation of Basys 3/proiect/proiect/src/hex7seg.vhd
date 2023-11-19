library	 IEEE;
use IEEE.STD_LOGIC_1164.all;
use IEEE.STD_LOGIC_unsigned.all;

entity hex7seg is
	port ( x: in STD_LOGIC_VECTOR(11 downto 0);
	clk : in STD_LOGIC;
	a_to_g : out STD_LOGIC_VECTOR(6 downto 0);
	an: out STD_LOGIC_VECTOR (3 downto 0);
	pornire: in STD_LOGIC);
end hex7seg;

architecture  hex7seg of hex7seg is
signal s: STD_LOGIC_VECTOR (1 downto 0);
signal digit: STD_LOGIC_VECTOR (3 downto 0);
signal aen: STD_LOGIC_VECTOR(3 downto 0);--folosit pentru activarea fiecaruia dintre leduri
signal aen_final: STD_LOGIC_VECTOR(3 downto 0);
signal clkdiv: STD_LOGIC_VECTOR(18 downto 0);  --ciobaneala, but whatever
begin 
	s<=clkdiv(18 downto 17);
	aen<="0111"; --doar ultimele 3 leduri sunt pornite
	
	--Multiplexorul de latime 4 biti
	process(s)
	begin
		case s is
		when "00" => digit<=x(3 downto 0);
		when "01" => digit<=x(7 downto 4);
		when others => digit<=x(11 downto 8);
		end case;
	end process;
	
	process(digit)
	begin
		case digit is
		when "0000" => 	a_to_g<="0000001";
		when "0001" => 	a_to_g<="1001111";
		when "0010" => 	a_to_g<="0010010";
		when "0011" => 	a_to_g<="0000110";
		when "0100" => 	a_to_g<="1001100";
		when "0101" => 	a_to_g<="0100100";
		when "0110" => 	a_to_g<="0100000";
		when "0111" => 	a_to_g<="0001101";
		when "1000" => 	a_to_g<="0000000";
		when "1001" => 	a_to_g<="0000100";
		when "1010" => 	a_to_g<="0001000";
		when "1011" => 	a_to_g<="1100000";
		when "1100" => 	a_to_g<="0110001";
		when "1101" => 	a_to_g<="1000010";
		when "1110" => 	a_to_g<="0110000";
		when others =>	a_to_g<="0111000";
		end case;
	end process;
	
	--Selectarea caracterului: ancode(tricky)
	process(s,aen,pornire)
	begin
		an<="1111"; --leduri dezactivate
		if aen(conv_integer(s))='1' then 
			if(pornire='1') then an(conv_integer(s))<='0';
					else an(conv_integer(s))<='1';
				end if;
		end if;
	end process;
	
	--Clock divider
	process(clk)
	begin
		if clk'event and clk='1' then
			clkdiv <=clkdiv+1;
		end if;
	end process;
end hex7seg;