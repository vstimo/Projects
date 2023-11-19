library IEEE;
use IEEE.STD_LOGIC_1164.all;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity principal is
	port(adauga_cifru: in STD_LOGIC;
	reset: in STD_LOGIC;
	clk: in STD_LOGIC;
	up: in STD_LOGIC;
	down: in STD_LOGIC;
	liber_ocupat: out STD_LOGIC :='0';
	introdu_caractere: out STD_LOGIC :='0';
	a_to_g: out STD_LOGIC_VECTOR(6 downto 0);
	an: out STD_LOGIC_VECTOR(3 downto 0));
end entity ;

architecture principal of principal is

component hex7seg_top is
	port(mclk: in STD_LOGIC; --clockul de la procesor
	x: in STD_LOGIC_VECTOR(11 downto 0);
	a_to_g : out STD_LOGIC_VECTOR(6 downto 0);--catozi
	an : out STD_LOGIC_VECTOR(3 downto 0); --anozi
	pornire:in std_logic);
end component;

component registru is 
	port(abcd   : in STD_LOGIC_VECTOR(3 downto 0);
    	load  : in STD_LOGIC; 
    	reset: in STD_LOGIC; 
    	clk : in STD_LOGIC; 
    	Q   : out STD_LOGIC_VECTOR(3 downto 0));
end component;

component carac_dis is
	port (reg1, reg2, reg3: in STD_LOGIC_VECTOR(3 downto 0);
	caractere_distincte: out STD_LOGIC);
end component; 

component numarator4 is 
   port( clk_enable: in std_logic;
 	 clk: in std_logic;
 	 reset: in std_logic;
 	 Q: out std_logic_vector(1 downto 0));
end component;

component comparator_cifre is
	port(reg1, reg2, reg3, reg4, reg5, reg6: in std_logic_vector(3 downto 0);
		comparator_cifre: out std_logic);
end component;

component numarator16v2 is 
   port( clk_up_enable: in std_logic;
   		 clk_down_enable: in std_logic;
 		 clk: in std_logic;
 		 reset: in std_logic;
		 reset_ac: in std_logic;
 	 	 q: out std_logic_vector(3 downto 0));
end component;

component divizor is
	port(clk:in std_logic;
		clk_nou: out std_logic);
end component;	

signal S4: std_logic_vector(1 downto 0);--IESIREA DIN NUMARATORUL N4 (4S,S4)   
signal ABCD: std_logic_vector(3 downto 0);--IESIREA DIN NUMARATORUL N16 
signal load:std_logic_vector(5 downto 0):="000000";	--semnal pentru a activa incarcarea paralela in registri
signal set1: std_logic_vector(11 downto 0); --registrii care formeaza blocarea cifrului
signal set2: std_logic_vector(11 downto 0);	--registrii care formeaza deblocarea cifrului
signal reg_afisor: std_logic_vector(11 downto 0); --semnal care iese din registri signal intra in afisor
signal liber_ocupat_sig: std_logic :='0';
signal distincte_caractere: std_logic :='0'; 
signal comp_de_cifre: std_logic :='0';

type stare_t is (Sreset,Sblocare,Sblocat,Sdeblocare);	 
signal stare,nxstare: stare_t;
signal reset_registri: std_logic;
signal reset_registri2: std_logic;
signal pornire: std_logic;
signal cmp_de_cifre_sig: std_logic;

begin 
N4: numarator4 port map(adauga_cifru,clk,reset,S4);
N16: numarator16v2 port map(up,down,clk,reset,adauga_cifru,ABCD); 

R1: registru port map(ABCD,load(0),reset_registri,clk,set1(3 downto 0));
R2: registru port map(ABCD,load(1),reset_registri,clk,set1(7 downto 4));	 
R3: registru port map(ABCD,load(2),reset_registri,clk,set1(11 downto 8));	
R4: registru port map(ABCD,load(3),reset_registri2,clk,set2(3 downto 0));
R5: registru port map(ABCD,load(4),reset_registri2,clk,set2(7 downto 4));	 
R6: registru port map(ABCD,load(5),reset_registri2,clk,set2(11 downto 8));	

C1: carac_dis port map(set1(3 downto 0),set1(7 downto 4),set1(11 downto 8),distincte_caractere);
C2: comparator_cifre port map(set1(3 downto 0),set1(7 downto 4),set1(11 downto 8),set2(3 downto 0),set2(7 downto 4),set2(11 downto 8),comp_de_cifre);	

A: hex7seg_top port map(clk,reg_afisor,a_to_g,an,pornire);

process(reset,clk,liber_ocupat_sig)
begin
	if(reset='1') then
		if(liber_ocupat_sig='0') then stare<=Sreset;  
			else stare<=Sblocat;
		end if;
	elsif clk='1' and clk'event then
		stare<=nxstare;
	end if;
end process;

process(stare,S4,distincte_caractere,comp_de_cifre)
begin
	case stare is
		when Sreset => liber_ocupat<='0'; liber_ocupat_sig<='0'; introdu_caractere<='0'; reset_registri<='1'; reset_registri2<='1'; pornire<='0';
		cmp_de_cifre_sig<='0';
		reg_afisor(3 downto 0)<=set1(3 downto 0); 
		reg_afisor(7 downto 4)<=set1(7 downto 4);
		reg_afisor(11 downto 8)<=set1(11 downto 8);
			if(S4="01") then nxstare<=Sblocare;
			else nxstare<=SReset;
			end if;	   
			
		when Sblocare=>
		cmp_de_cifre_sig<='0';
		reg_afisor(3 downto 0)<=set1(3 downto 0); 
		reg_afisor(7 downto 4)<=set1(7 downto 4);
		reg_afisor(11 downto 8)<=set1(11 downto 8);
		pornire<='1';
		case S4 is
			when "00" => introdu_caractere<='1'; load(0)<='0'; load(1)<='0';  load(2)<='0'; load(3)<='0'; load(4)<='0'; load(5)<='0';  
			if(distincte_caractere='1')then reset_registri<='0';
				else reset_registri<='1';
			end if;
			when "01" => introdu_caractere<='1'; load(0)<='1'; load(1)<='0';  load(2)<='0'; load(3)<='0'; load(4)<='0'; load(5)<='0'; reset_registri<='0';
			when "10" => introdu_caractere<='1'; load(0)<='0'; load(1)<='1';  load(2)<='0'; load(3)<='0'; load(4)<='0'; load(5)<='0'; reset_registri<='0'; 
			when others => introdu_caractere<='1'; load(0)<='0'; load(1)<='0';  load(2)<='1'; load(3)<='0'; load(4)<='0'; load(5)<='0'; reset_registri<='0';
		end case;
		if(distincte_caractere='1' and S4="00") then
			nxstare<=Sblocat;
		else nxstare<=Sblocare; 
		end if;
		
		when Sblocat => liber_ocupat_sig<='1'; reset_registri<='0';	reset_registri2<='1';  	introdu_caractere<='0';
			cmp_de_cifre_sig<='0';
			reg_afisor(3 downto 0)<=set2(3 downto 0);			 
			reg_afisor(7 downto 4)<=set2(7 downto 4);
			reg_afisor(11 downto 8)<=set2(11 downto 8);
			pornire<='0';
			if(S4="01") then nxstare<=Sdeblocare;
			else nxstare<=Sblocat;
			end if;
			
			
		when Sdeblocare=>  
			reg_afisor(3 downto 0)<=set2(3 downto 0);			 
			reg_afisor(7 downto 4)<=set2(7 downto 4);
			reg_afisor(11 downto 8)<=set2(11 downto 8);	
			
			cmp_de_cifre_sig<=comp_de_cifre;
		case S4 is
			when "00" => introdu_caractere<='1'; load(0)<='0'; load(1)<='0';  load(2)<='0'; load(3)<='0'; load(4)<='0'; load(5)<='0';
				if(comp_de_cifre='1') then reset_registri2<='0';
				else reset_registri2<='1';
				end if;
			when "01" => introdu_caractere<='1'; load(0)<='0'; load(1)<='0';  load(2)<='0'; load(3)<='1'; load(4)<='0'; load(5)<='0'; reset_registri2<='0';
			when "10" => introdu_caractere<='1'; load(0)<='0'; load(1)<='0';  load(2)<='0'; load(3)<='0'; load(4)<='1'; load(5)<='0'; reset_registri2<='0';
			when others => introdu_caractere<='1'; load(0)<='0'; load(1)<='0';  load(2)<='0'; load(3)<='0'; load(4)<='0'; load(5)<='1'; reset_registri2<='0';
		end case;
		liber_ocupat<='1';
		if(comp_de_cifre='1' and S4="00") then
			nxstare<=SReset;
		else nxstare<=Sdeblocare;
		end if;
	end case;
end process;
end architecture ;	 