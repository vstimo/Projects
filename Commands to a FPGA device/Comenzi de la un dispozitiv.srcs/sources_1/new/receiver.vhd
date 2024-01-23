library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use ieee.NUMERIC_STD.all;

entity receiver is
    port (clk, rx: in  std_logic;
         rxRdy: out std_logic;
         data: out std_logic_vector(7 downto 0));
end receiver;

architecture rtl of receiver is
    constant TICK: integer:=868; --baud rate of 115_200
    type state is (idle, start, receive, stop, cleanup);
    signal stare : state := idle;

    signal signalRx : std_logic := '0';
    signal signalRxData   : std_logic := '0';
    signal clkCount : integer range 0 to TICK-1 := 0;
    signal bitIndex : integer range 0 to 7 := 0;
    signal dataRegister: std_logic_vector(7 downto 0) := (others => '0');
    signal signalDone: std_logic := '0';

begin
    -- Purpose: Double-register the incoming data.
    -- This allows it to be used in the UART RX Clock Domain.
    -- (It removes problems caused by metastabiliy)
    process (clk)
    begin
        if rising_edge(clk) then
            signalRx <= rx;
            signalRxData<=signalRx;
        end if;
    end process;

    process (clk)
    begin
        if rising_edge(clk) then
            case stare is
                when idle =>
                    signalDone<= '0'; clkCount <= 0;
                    bitIndex <= 0;
                    if signalRxData = '0' then       -- Start bit detected
                        stare <= start;
                    else
                        stare <= idle;
                    end if;

                -- Check start of start bit to make sure it's still low
                when start =>
                    if clkCount = (TICK-1)/2 then
                        if signalRxData = '0' then
                            clkCount <= 0;  -- reset counter since we found the middle
                            stare<= receive;
                        else
                            stare<= idle;
                        end if;
                    else
                        clkCount<=clkCount + 1;
                        stare<=start;
                    end if;

                -- Wait TICK-1 clock cycles to sample serial data
                when receive =>
                    if clkCount < TICK-1 then
                        clkCount <= clkCount + 1;
                        stare <= receive;
                    else
                        clkCount <= 0;
                        dataRegister(bitIndex) <= signalRxData;

                        -- Check if we have sent out all bits
                        if bitIndex < 7 then
                            bitIndex <= bitIndex + 1;
                            stare   <= receive;
                        else
                            bitIndex <= 0;
                            stare <= stop;
                        end if;
                    end if;

                when stop =>
                    -- Wait TICK-1 clock cycles for Stop bit to finish
                    if clkCount < TICK-1 then
                        clkCount <= clkCount + 1;
                        stare   <= stop;
                    else
                        signalDone<= '1';
                        clkCount <= 0;
                        stare <= idle;
                    end if;
                when others =>
                    stare <= idle;
            end case;
        end if;
    end process;

    rxRdy <= signalDone;
    data <= dataRegister;
end rtl;